package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.entity.EstadoCosecha;
import cu.edu.unah.GuayabalSiSDE.entity.PlanificacionCosecha;
import cu.edu.unah.GuayabalSiSDE.repository.PlanificacionCosechaRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import cu.edu.unah.GuayabalSiSDE.util.PlanificacionCosechaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlanificacionCosechaServiceImpl implements PlanificacionCosechaService {

    @Autowired
    private PlanificacionCosechaRepository planificacionCosechaRepository;

    @Autowired
    private AreaCultivoService areaCultivoService;

    @Override
    @Transactional
    public List<PlanificacionCosechaResponse> findCalendario(Date desde, Date hasta) {
        List<AreaCultivo> lista = areaCultivoService.findByFechaRecogidaBetween(desde, hasta);
        List<PlanificacionCosechaResponse> resultado = new ArrayList<>();
        for (AreaCultivo ac : lista) {
            PlanificacionCosecha pc = findOCrear(ac);
            resultado.add(PlanificacionCosechaResponse.map(pc, ac));
        }
        return resultado;
    }

    @Override
    @Transactional
    public PlanificacionCosechaResponse completar(AreaCultivoPk pk, Double produccionReal) {
        if (produccionReal == null || produccionReal < 0) {
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD,
                    "La producción real debe ser un valor mayor o igual a cero.");
        }

        AreaCultivo ac = areaCultivoService.findById(pk);
        if (ac == null) {
            throw new BusinessValidationException(ErrorCodes.ENTITY_NOT_FOUND,
                    "No existe la planificación de cosecha solicitada.");
        }

        ac.setProduccionReal(produccionReal);
        AreaCultivo actualizado = areaCultivoService.edit(ac);

        PlanificacionCosecha pc = findOCrear(actualizado);
        pc.setEstado(EstadoCosecha.COMPLETADA);
        pc.setFechaCompletado(LocalDateTime.now());
        pc.setFechaEstimada(actualizado.getFechaRecogida());
        pc.setCumplimientoPct(actualizado.getPlanProd() != null && actualizado.getPlanProd() > 0
                ? (produccionReal / actualizado.getPlanProd()) * 100.0
                : null);

        PlanificacionCosecha guardado = planificacionCosechaRepository.save(pc);
        return PlanificacionCosechaResponse.map(guardado, actualizado);
    }

    @Override
    @Transactional
    public PlanificacionCosechaResponse cancelar(AreaCultivoPk pk) {
        AreaCultivo ac = areaCultivoService.findById(pk);
        if (ac == null) {
            throw new BusinessValidationException(ErrorCodes.ENTITY_NOT_FOUND,
                    "No existe la planificación de cosecha solicitada.");
        }

        PlanificacionCosecha pc = findOCrear(ac);
        pc.setEstado(EstadoCosecha.CANCELADA);
        PlanificacionCosecha guardado = planificacionCosechaRepository.save(pc);
        return PlanificacionCosechaResponse.map(guardado, ac);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 5 0 * * *")
    public void actualizarVencidas() {
        Date hoy = Date.valueOf(LocalDate.now());
        List<PlanificacionCosecha> vencidas = planificacionCosechaRepository
                .findByEstadoAndFechaEstimadaBefore(EstadoCosecha.PLANIFICADA, hoy);
        for (PlanificacionCosecha pc : vencidas) {
            pc.setEstado(EstadoCosecha.VENCIDA);
        }
        planificacionCosechaRepository.saveAll(vencidas);
    }

    private PlanificacionCosecha findOCrear(AreaCultivo ac) {
        return planificacionCosechaRepository.findById(ac.getAreaCultivoPk())
                .orElseGet(() -> crearDesde(ac));
    }

    private PlanificacionCosecha crearDesde(AreaCultivo ac) {
        EstadoCosecha estadoInicial;
        if (ac.getProduccionReal() != null && ac.getProduccionReal() > 0) {
            estadoInicial = EstadoCosecha.COMPLETADA;
        } else if (ac.getFechaRecogida() != null && ac.getFechaRecogida().before(Date.valueOf(LocalDate.now()))) {
            estadoInicial = EstadoCosecha.VENCIDA;
        } else {
            estadoInicial = EstadoCosecha.PLANIFICADA;
        }

        Double cumplimiento = null;
        if (estadoInicial == EstadoCosecha.COMPLETADA && ac.getPlanProd() != null && ac.getPlanProd() > 0) {
            cumplimiento = (ac.getProduccionReal() / ac.getPlanProd()) * 100.0;
        }

        PlanificacionCosecha nuevo = PlanificacionCosecha.builder()
                .areaCultivoPk(ac.getAreaCultivoPk())
                .estado(estadoInicial)
                .fechaEstimada(ac.getFechaRecogida())
                .cumplimientoPct(cumplimiento)
                .build();
        return planificacionCosechaRepository.save(nuevo);
    }
}
