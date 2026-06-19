package cu.edu.unah.GuayabalSiSDE.repository;

import cu.edu.unah.GuayabalSiSDE.entity.MovimientoAgroquimico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoAgroquimicoRepository extends JpaRepository<MovimientoAgroquimico, Long> {

    List<MovimientoAgroquimico> findByAgroquimicoIdOrderByFechaDesc(Long agroquimicoId);
}
