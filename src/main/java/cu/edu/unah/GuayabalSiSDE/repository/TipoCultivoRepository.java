package cu.edu.unah.GuayabalSiSDE.repository;

import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoCultivoRepository extends JpaRepository<TipoCultivo, Long> {

    TipoCultivo findByNombre(String nombre);
}
