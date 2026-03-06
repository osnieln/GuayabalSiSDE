package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.entity.Riego;

import java.util.List;

public interface RiegoService {

    List<Riego> findAll();
    Riego findById(long id);
    List<Riego> findByAreaCultivoPk(AreaCultivoPk areaCultivoPk);
    Riego create(Riego riego);
    Riego edit(Riego riego);
    Riego delete(long id);

    List<Riego> findProximosRiegos(int dias);

    List<Riego> findHistorialByAreaId(Long areaId);

    List<Riego> findRiegosConDesviacion(int diasMaximos);
}
