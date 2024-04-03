package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Area;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.repository.AreaCultivoRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class AreaCultivoServiceImpl implements AreaCultivoService{

    @Autowired
    AreaCultivoRepository areaCultivoRepository;

    @Autowired
    @Lazy
    AreaService areaService;

    @Autowired
    @Lazy
    CultivoService cultivoService;

    @Override
    public List<AreaCultivo> findAll() {
        return areaCultivoRepository.findAll();
    }

    @Override
    public AreaCultivo findById(@NonNull AreaCultivoPk areaCultivoPk) {
        return areaCultivoRepository.findById(areaCultivoPk).orElse(null);
    }

    @Override
    public AreaCultivo create(@NonNull AreaCultivo areaCultivo) {
        AreaCultivo areaCultivoDb = findById(areaCultivo.getAreaCultivoPk());
        if(areaCultivoDb!=null)
            return null;
        Area areaDb = areaService.findByID(areaCultivo.getAreaCultivoPk().getAreaId());
        Cultivo cultivoDb = cultivoService.findById(areaCultivo.getAreaCultivoPk().getCultivoId());
        if(null == areaDb || null == cultivoDb)
            return null;
        areaCultivo.setCultivo(cultivoDb);
        areaCultivo.setArea(areaDb);
        return areaCultivoRepository.save(areaCultivo);
    }

    @Override
    public AreaCultivo edit(@NonNull AreaCultivo areaCultivo) {
        AreaCultivo areaCultivoDb = findById(areaCultivo.getAreaCultivoPk());
        if(areaCultivoDb==null)
            return null;
        areaCultivoDb.setProdCultivosTemporales(areaCultivo.getProdCultivosTemporales());
        areaCultivoDb.setProdCultivosPermanente(areaCultivo.getProdCultivosPermanente());
        areaCultivoDb.setPlanProd(areaCultivo.getPlanProd());
        areaCultivoDb.setArea(areaCultivo.getArea());
        areaCultivoDb.setProduccionReal(areaCultivo.getProduccionReal());
        areaCultivoDb.setFechaRecogida(areaCultivo.getFechaRecogida());
        return areaCultivoRepository.save(areaCultivoDb);
    }

    @Override
    public AreaCultivo delete(@NonNull AreaCultivoPk areaCultivoPk) {
        AreaCultivo areaCultivoDb = findById(areaCultivoPk);
        if(areaCultivoDb==null)
            return null;
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
