package cu.edu.unah.GuayabalSiSDE.repository;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface AreaCultivoRepository extends JpaRepository<AreaCultivo, AreaCultivoPk> {

    List<AreaCultivo> findAreaCultivoByPlanProdBetween(Long planProd, Long planProd2);

    List<AreaCultivo> findAreaCultivoByProdCultivosPermanenteAfter(Double prodCultivosPermanente);

    List<AreaCultivo> findAreaCultivoByFechaRecogidaBefore(Date fechaRecogida);
}
