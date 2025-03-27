package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponse;

import java.sql.Date;
import java.util.List;

public interface AreaCultivoService {

    List<AreaCultivo> findAll();
    AreaCultivo findById(AreaCultivoPk areaCultivoPk);
    AreaCultivo create(AreaCultivo areaCultivo);
    AreaCultivo edit(AreaCultivo areaCultivo);
    AreaCultivo delete(AreaCultivoPk areaCultivoPk);
    List<AreaCultivo> findAreaCultivoByPlanProdBetween(Long planProd, Long planProd2);
    List<AreaCultivo> findAreaCultivoByProdCultivosPermanenteAfter(Double prodCultivosPermanente);
    List<AreaCultivo> findAreaCultivoByFechaRecogidaBefore(Date fechaRecogida);

}
