package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;

import java.util.List;

public interface AreaCultivoService {

    List<AreaCultivo> findAll();
    AreaCultivo findById(AreaCultivoPk areaCultivoPk);
    AreaCultivo create(AreaCultivo areaCultivo);
    AreaCultivo edit(AreaCultivo areaCultivo);
    AreaCultivo delete(AreaCultivoPk areaCultivoPk);
}
