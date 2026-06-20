package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.*;
import cu.edu.unah.GuayabalSiSDE.repository.AreaCultivoRepository;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponse;
import cu.edu.unah.GuayabalSiSDE.util.CultivoDistribucionResponse;
import cu.edu.unah.GuayabalSiSDE.util.DateFormatter;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import cu.edu.unah.GuayabalSiSDE.util.ProduccionMensualResponse;
import cu.edu.unah.GuayabalSiSDE.util.RendimientoResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AreaCultivoServiceImpl implements AreaCultivoService {

    @Autowired
    AreaCultivoRepository areaCultivoRepository;

    @Autowired
    @Lazy
    AreaService areaService;

    @Autowired
    @Lazy
    CultivoService cultivoService;

    @Autowired
    @Lazy
    AgroquimicoService agroquimicoService;

    String className = "AreaCultivo";

    @Override
    public List<AreaCultivo> findAll() {
        return areaCultivoRepository.findAll();
    }

    @Override
    public AreaCultivo findById(@NonNull AreaCultivoPk areaCultivoPk) {
        areaCultivoPk.setFechaSiembra(new Date(areaCultivoPk.getFechaSiembra().getTime()));
        return areaCultivoRepository.findById(areaCultivoPk).orElse(null);
    }

    @Override
    public AreaCultivo create(@NonNull AreaCultivo areaCultivo) {
        AreaCultivo areaCultivoDb = findById(areaCultivo.getAreaCultivoPk());
        if (areaCultivoDb != null) {
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "Esta área ya existe.");
        }
        Area areaDb = areaService.findByID(areaCultivo.getAreaCultivoPk().getAreaId());
        Cultivo cultivoDb = cultivoService.findById(areaCultivo.getAreaCultivoPk().getCultivoId());
        if (null == areaDb || null == cultivoDb) {
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "Esta área no existe.");
        }
        List<Agroquimico> agroquimicoList = areaCultivo.getAgroquimicos();
        List<Agroquimico> agroquimicoListDb = new ArrayList<>();
        agroquimicoList.forEach(agroquimico -> {
            Agroquimico agroquimicoDb = agroquimicoService.findByNombre(agroquimico.getNombre());
            if (null != agroquimicoDb) {
                agroquimicoListDb.add(agroquimicoDb);
            }
        });
        areaCultivo.setAgroquimicos(agroquimicoListDb);
        areaCultivo.setCultivo(cultivoDb);
        areaCultivo.setArea(areaDb);
        return areaCultivoRepository.save(areaCultivo);
    }

    @Override
    public AreaCultivo edit(@NonNull AreaCultivo areaCultivo) {
        AreaCultivo areaCultivoDb = findById(areaCultivo.getAreaCultivoPk());
        if (areaCultivoDb == null) {
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "Esta área no existe.");
        }
        areaCultivoDb.setProdCultivosTemporales(areaCultivo.getProdCultivosTemporales());
        areaCultivoDb.setProdCultivosPermanente(areaCultivo.getProdCultivosPermanente());
        areaCultivoDb.setPlanProd(areaCultivo.getPlanProd());
        areaCultivoDb.setArea(areaCultivo.getArea());
        areaCultivoDb.setProduccionReal(areaCultivo.getProduccionReal());
        areaCultivoDb.setFechaRecogida(areaCultivo.getFechaRecogida());
        List<Agroquimico> agroquimicoList = areaCultivo.getAgroquimicos();
        List<Agroquimico> agroquimicoListDb = new ArrayList<>();
        agroquimicoList.forEach(agroquimico -> {
            Agroquimico agroquimicoDb = agroquimicoService.findByNombre(agroquimico.getNombre());
            if (null != agroquimicoDb) {
                agroquimicoListDb.add(agroquimicoDb);
            }
        });
        areaCultivo.setAgroquimicos(agroquimicoListDb);
        return areaCultivoRepository.save(areaCultivoDb);
    }

    @Override
    public AreaCultivo delete(@NonNull AreaCultivoPk areaCultivoPk) {
        AreaCultivo areaCultivoDb = findById(areaCultivoPk);
        if (areaCultivoDb == null) {
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "Esta área no existe.");
        }
        areaCultivoDb.getAgroquimicos().size();
        areaCultivoRepository.delete(areaCultivoDb);
        return areaCultivoDb;
    }

    @Override
    public List<AreaCultivo> findAreaCultivoByPlanProdBetween(Long planProd, Long planProd2) {
        return areaCultivoRepository.findAreaCultivoByPlanProdBetween(planProd, planProd2);
    }

    @Override
    public List<AreaCultivo> findAreaCultivoByProdCultivosPermanenteAfter(Double prodCultivosPermanente) {
        return areaCultivoRepository.findAreaCultivoByProdCultivosPermanenteAfter(prodCultivosPermanente);
    }

    @Override
    public List<AreaCultivo> findAreaCultivoByFechaRecogidaBefore(Date fechaRecogida) {
        return areaCultivoRepository.findAreaCultivoByFechaRecogidaBefore(fechaRecogida);
    }

    @Override
    public List<AreaCultivo> findByActivo(boolean activo) {
        return areaCultivoRepository.findByActivo(activo);
    }

    @Override
    public List<AreaCultivo> findCultivosPorVencer(int dias) {
        Date hoy = Date.valueOf(LocalDate.now());
        Date limite = Date.valueOf(LocalDate.now().plusDays(dias));
        return areaCultivoRepository.findAreaCultivoByFechaRecogidaBetween(hoy, limite);
    }

    @Override
    public List<RendimientoResponse> calcularRendimiento() {
        List<AreaCultivo> todos = areaCultivoRepository.findAll();
        List<RendimientoResponse> resultado = new ArrayList<>();
        for (AreaCultivo ac : todos) {
            Double rendimiento = null;
            if (ac.getPlanProd() != null && ac.getPlanProd() > 0 && ac.getProduccionReal() != null) {
                rendimiento = (ac.getProduccionReal() / ac.getPlanProd()) * 100.0;
            }
            resultado.add(RendimientoResponse.builder()
                    .areaId(ac.getAreaCultivoPk().getAreaId())
                    .areaDescripcion(ac.getArea() != null ? ac.getArea().getDescripcion() : null)
                    .cultivoId(ac.getAreaCultivoPk().getCultivoId())
                    .cultivoDescripcion(ac.getCultivo() != null ? ac.getCultivo().getDescripcion() : null)
                    .fechaSiembra(DateFormatter.format(ac.getAreaCultivoPk().getFechaSiembra()))
                    .planProd(ac.getPlanProd())
                    .produccionReal(ac.getProduccionReal())
                    .rendimientoPorcentaje(rendimiento)
                    .build());
        }
        return resultado;
    }

    private static final String[] MESES_ABREV = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};

    @Override
    @Transactional(readOnly = true)
    public List<ProduccionMensualResponse> findProduccionMensual(int meses) {
        LocalDate inicio = LocalDate.now().minusMonths(meses - 1L).withDayOfMonth(1);
        Map<YearMonth, Double> totales = new LinkedHashMap<>();
        for (int i = 0; i < meses; i++) {
            totales.put(YearMonth.from(inicio.plusMonths(i)), 0.0);
        }
        areaCultivoRepository.findProduccionMensualDesde(Date.valueOf(inicio)).forEach(row -> {
            YearMonth ym = YearMonth.of(((Number) row[0]).intValue(), ((Number) row[1]).intValue());
            totales.put(ym, ((Number) row[2]).doubleValue());
        });
        return totales.entrySet().stream()
                .map(e -> ProduccionMensualResponse.builder()
                        .mes(MESES_ABREV[e.getKey().getMonthValue() - 1] + " " + e.getKey().getYear())
                        .total(e.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CultivoDistribucionResponse> findDistribucionCultivos() {
        return areaCultivoRepository.findDistribucionPorCultivo().stream()
                .map(row -> CultivoDistribucionResponse.builder()
                        .cultivo((String) row[0])
                        .cantidad(((Number) row[1]).longValue())
                        .build())
                .collect(Collectors.toList());
    }
}
