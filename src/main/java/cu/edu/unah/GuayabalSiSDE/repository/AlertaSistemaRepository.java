package cu.edu.unah.GuayabalSiSDE.repository;

import cu.edu.unah.GuayabalSiSDE.entity.AlertaSistema;
import cu.edu.unah.GuayabalSiSDE.entity.TipoAlerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaSistemaRepository extends JpaRepository<AlertaSistema, Long> {

    List<AlertaSistema> findByResueltaFalseOrderByFechaGeneracionDesc();

    List<AlertaSistema> findByLeidaFalseAndResueltaFalseOrderByFechaGeneracionDesc();

    List<AlertaSistema> findByTipoAndResueltaFalse(TipoAlerta tipo);

    long countByResueltaFalse();
}
