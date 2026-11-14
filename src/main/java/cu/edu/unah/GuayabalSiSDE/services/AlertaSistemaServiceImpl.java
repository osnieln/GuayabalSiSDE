package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.*;
import cu.edu.unah.GuayabalSiSDE.repository.AlertaSistemaRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AlertaSistemaServiceImpl implements AlertaSistemaService {

    private static final int DEFAULT_DIAS_CULTIVO = 7;
    private static final int DEFAULT_DIAS_RIEGO = 7;

    @Autowired
    private AlertaSistemaRepository alertaSistemaRepository;

    @Autowired
    private AreaCultivoService areaCultivoService;

    @Autowired
    private RiegoService riegoService;

    @Autowired
    private AgroquimicoService agroquimicoService;

    @Override
    @Transactional
    public List<AlertaSistema> findAll(boolean soloNoLeidas) {
        return soloNoLeidas
                ? alertaSistemaRepository.findByLeidaFalseAndResueltaFalseOrderByFechaGeneracionDesc()
                : alertaSistemaRepository.findByResueltaFalseOrderByFechaGeneracionDesc();
    }

    @Override
    @Transactional
    public long countActivas() {
        return alertaSistemaRepository.countByResueltaFalse();
    }

    @Override
    @Transactional
    public AlertaSistema marcarLeida(Long id) {
        AlertaSistema alerta = buscarPorId(id);
        alerta.setLeida(true);
        alerta.setFechaLectura(LocalDateTime.now());
        return alertaSistemaRepository.save(alerta);
    }

    @Override
    @Transactional
    public AlertaSistema marcarResuelta(Long id) {
        AlertaSistema alerta = buscarPorId(id);
        alerta.setResuelta(true);
        alerta.setFechaResolucion(LocalDateTime.now());
        return alertaSistemaRepository.save(alerta);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void generarAlertas() {
        sincronizarAlertasAreaCultivo(
                TipoAlerta.CULTIVO_POR_VENCER,
                PrioridadAlerta.ALTA,
                areaCultivoService.findCultivosPorVencer(DEFAULT_DIAS_CULTIVO),
                ac -> "El cultivo " + nombreCultivo(ac) + " en el área " + nombreArea(ac)
                        + " está próximo a su fecha de recogida.");

        sincronizarAlertasAreaCultivo(
                TipoAlerta.RIEGO_PENDIENTE,
                PrioridadAlerta.MEDIA,
                riegoService.findAreasSinRiego(DEFAULT_DIAS_RIEGO),
                ac -> "El área " + nombreArea(ac) + " (cultivo " + nombreCultivo(ac)
                        + ") lleva más de " + DEFAULT_DIAS_RIEGO + " días sin riego registrado.");

        sincronizarAlertasAgroquimico(agroquimicoService.findBajoStock());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void generarAlertasAlIniciar() {
        generarAlertas();
    }

    private void sincronizarAlertasAreaCultivo(TipoAlerta tipo, PrioridadAlerta prioridad,
                                                List<AreaCultivo> activos,
                                                Function<AreaCultivo, String> mensaje) {
        List<AlertaSistema> existentes = alertaSistemaRepository.findByTipoAndResueltaFalse(tipo);
        Set<String> clavesActivas = new HashSet<>();

        for (AreaCultivo ac : activos) {
            AreaCultivoPk pk = ac.getAreaCultivoPk();
            String clave = claveAreaCultivo(pk.getAreaId(), pk.getCultivoId(), pk.getFechaSiembra());
            clavesActivas.add(clave);

            boolean existe = existentes.stream()
                    .anyMatch(a -> clave.equals(claveAreaCultivo(a.getAreaId(), a.getCultivoId(), a.getFechaSiembra())));

            if (!existe) {
                alertaSistemaRepository.save(AlertaSistema.builder()
                        .tipo(tipo)
                        .prioridad(prioridad)
                        .mensaje(mensaje.apply(ac))
                        .fechaReferencia(ac.getFechaRecogida())
                        .areaId(pk.getAreaId())
                        .cultivoId(pk.getCultivoId())
                        .fechaSiembra(pk.getFechaSiembra())
                        .fechaGeneracion(LocalDateTime.now())
                        .build());
            }
        }

        for (AlertaSistema a : existentes) {
            String clave = claveAreaCultivo(a.getAreaId(), a.getCultivoId(), a.getFechaSiembra());
            if (!clavesActivas.contains(clave)) {
                a.setResuelta(true);
                a.setFechaResolucion(LocalDateTime.now());
                alertaSistemaRepository.save(a);
            }
        }
    }

    private void sincronizarAlertasAgroquimico(List<Agroquimico> bajoStock) {
        List<AlertaSistema> existentes = alertaSistemaRepository.findByTipoAndResueltaFalse(TipoAlerta.AGROQUIMICO_BAJO_STOCK);
        Set<Long> idsActivos = new HashSet<>();

        for (Agroquimico a : bajoStock) {
            idsActivos.add(a.getId());
            boolean existe = existentes.stream().anyMatch(al -> a.getId().equals(al.getAgroquimicoId()));

            if (!existe) {
                alertaSistemaRepository.save(AlertaSistema.builder()
                        .tipo(TipoAlerta.AGROQUIMICO_BAJO_STOCK)
                        .prioridad(PrioridadAlerta.MEDIA)
                        .mensaje("El agroquímico \"" + a.getNombre() + "\" tiene existencia baja ("
                                + a.getStockActual() + " de mínimo " + a.getStockMinimo() + ").")
                        .agroquimicoId(a.getId())
                        .fechaGeneracion(LocalDateTime.now())
                        .build());
            }
        }

        for (AlertaSistema a : existentes) {
            if (!idsActivos.contains(a.getAgroquimicoId())) {
                a.setResuelta(true);
                a.setFechaResolucion(LocalDateTime.now());
                alertaSistemaRepository.save(a);
            }
        }
    }

    private String claveAreaCultivo(Long areaId, Long cultivoId, java.sql.Date fechaSiembra) {
        return areaId + "|" + cultivoId + "|" + fechaSiembra;
    }

    private AlertaSistema buscarPorId(Long id) {
        return alertaSistemaRepository.findById(id)
                .orElseThrow(() -> new BusinessValidationException(ErrorCodes.ENTITY_NOT_FOUND,
                        "No existe la alerta con id " + id + "."));
    }

    private String nombreCultivo(AreaCultivo ac) {
        Cultivo c = ac.getCultivo();
        return c != null && c.getDescripcion() != null ? c.getDescripcion() : "N/D";
    }

    private String nombreArea(AreaCultivo ac) {
        Area a = ac.getArea();
        return a != null && a.getDescripcion() != null ? a.getDescripcion() : "N/D";
    }
}
