package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.*;
import cu.edu.unah.GuayabalSiSDE.repository.AreaCultivoRepository;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponse;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
}
