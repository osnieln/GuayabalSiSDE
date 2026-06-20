package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponse;
import cu.edu.unah.GuayabalSiSDE.util.CultivoDistribucionResponse;
import cu.edu.unah.GuayabalSiSDE.util.ProduccionMensualResponse;
import cu.edu.unah.GuayabalSiSDE.util.RendimientoResponse;

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
    List<AreaCultivo> findByActivo(boolean activo);
    List<RendimientoResponse> calcularRendimiento();

    List<AreaCultivo> findCultivosPorVencer(int dias);

    List<ProduccionMensualResponse> findProduccionMensual(int meses);

    List<CultivoDistribucionResponse> findDistribucionCultivos();
}
