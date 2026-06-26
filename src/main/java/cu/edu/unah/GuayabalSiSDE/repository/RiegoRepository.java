package cu.edu.unah.GuayabalSiSDE.repository;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.entity.Riego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface RiegoRepository extends JpaRepository<Riego, Long> {

    List<Riego> findRiegoByAreaCultivo_AreaCultivoPk(AreaCultivoPk areaCultivoAreaCultivoPk);

    List<Riego> findByFechaPlanificacionBetweenAndFechaRealIsNull(Date inicio, Date fin);

    List<Riego> findByAreaCultivo_AreaCultivoPk_AreaIdAndFechaRealIsNotNull(Long areaId);

    List<Riego> findByAreaCultivo_AreaCultivoPk_AreaId(Long areaId);

    @Query("""
        SELECT ac FROM AreaCultivo ac
        WHERE ac.activo = true
        AND (
            (SELECT MAX(r.fechaReal) FROM Riego r WHERE r.areaCultivo.areaCultivoPk = ac.areaCultivoPk) IS NULL
            OR (SELECT MAX(r.fechaReal) FROM Riego r WHERE r.areaCultivo.areaCultivoPk = ac.areaCultivoPk) < :fechaLimite
        )
    """)
    List<AreaCultivo> findAreasSinRiegoDesde(@Param("fechaLimite") Date fechaLimite);

    @Query("""
        SELECT extract(year from r.fechaPlanificacion), extract(month from r.fechaPlanificacion), COUNT(r),
               SUM(CASE WHEN r.fechaReal IS NOT NULL THEN 1 ELSE 0 END)
        FROM Riego r
        WHERE r.fechaPlanificacion >= :desde
        GROUP BY extract(year from r.fechaPlanificacion), extract(month from r.fechaPlanificacion)
        ORDER BY extract(year from r.fechaPlanificacion), extract(month from r.fechaPlanificacion)
    """)
    List<Object[]> findRiegosMensualesDesde(@Param("desde") Date desde);
}
