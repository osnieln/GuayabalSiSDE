package cu.edu.unah.GuayabalSiSDE.repository;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.entity.EstadoCosecha;
import cu.edu.unah.GuayabalSiSDE.entity.PlanificacionCosecha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface PlanificacionCosechaRepository extends JpaRepository<PlanificacionCosecha, AreaCultivoPk> {

    List<PlanificacionCosecha> findByEstado(EstadoCosecha estado);

    List<PlanificacionCosecha> findByEstadoAndFechaEstimadaBefore(EstadoCosecha estado, Date fecha);
}
