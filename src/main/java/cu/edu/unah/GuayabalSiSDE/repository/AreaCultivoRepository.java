package cu.edu.unah.GuayabalSiSDE.repository;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface AreaCultivoRepository extends JpaRepository<AreaCultivo, AreaCultivoPk> {

    List<AreaCultivo> findAreaCultivoByPlanProdBetween(Long planProd, Long planProd2);

    List<AreaCultivo> findAreaCultivoByProdCultivosPermanenteAfter(Double prodCultivosPermanente);

    List<AreaCultivo> findAreaCultivoByFechaRecogidaBefore(Date fechaRecogida);

    List<AreaCultivo> findByActivo(boolean activo);

    List<AreaCultivo> findAreaCultivoByFechaRecogidaBetween(Date fechaInicio, Date fechaFin);

    @Query("""
        SELECT extract(year from ac.fechaRecogida), extract(month from ac.fechaRecogida), SUM(ac.produccionReal)
        FROM AreaCultivo ac
        WHERE ac.fechaRecogida IS NOT NULL AND ac.fechaRecogida >= :desde
        GROUP BY extract(year from ac.fechaRecogida), extract(month from ac.fechaRecogida)
        ORDER BY extract(year from ac.fechaRecogida), extract(month from ac.fechaRecogida)
    """)
    List<Object[]> findProduccionMensualDesde(@Param("desde") Date desde);

    @Query("SELECT ac.cultivo.descripcion, COUNT(ac) FROM AreaCultivo ac WHERE ac.activo = true GROUP BY ac.cultivo.descripcion ORDER BY COUNT(ac) DESC")
    List<Object[]> findDistribucionPorCultivo();
}
