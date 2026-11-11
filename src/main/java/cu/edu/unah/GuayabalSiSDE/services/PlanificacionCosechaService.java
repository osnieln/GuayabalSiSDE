package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.util.PlanificacionCosechaResponse;

import java.sql.Date;
import java.util.List;

public interface PlanificacionCosechaService {

    List<PlanificacionCosechaResponse> findCalendario(Date desde, Date hasta);

    PlanificacionCosechaResponse completar(AreaCultivoPk pk, Double produccionReal);

    PlanificacionCosechaResponse cancelar(AreaCultivoPk pk);

    void actualizarVencidas();
}
