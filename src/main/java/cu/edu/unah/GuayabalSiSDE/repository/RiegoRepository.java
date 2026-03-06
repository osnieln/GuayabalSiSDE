package cu.edu.unah.GuayabalSiSDE.repository;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.entity.Riego;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface RiegoRepository extends JpaRepository<Riego, Long> {

    List<Riego> findRiegoByAreaCultivo_AreaCultivoPk(AreaCultivoPk areaCultivoAreaCultivoPk);

    List<Riego> findByFechaPlanificacionBetween(Date inicio, Date fin);

    List<Riego> findByAreaCultivo_Area_IdAndFechaRealIsNotNull(Long areaId);
}
