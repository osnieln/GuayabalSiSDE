package cu.edu.unah.GuayabalSiSDE.repository;

import cu.edu.unah.GuayabalSiSDE.entity.Agroquimico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgroquimicoRepository extends JpaRepository<Agroquimico, Long> {

//    @Query("SELECT DISTINCT a FROM Agroquimico a LEFT JOIN FETCH a.areaCultivos")
//    List<Agroquimico> findAllWithAreaCultivos();

    Agroquimico findByNombre(String nombre);
}
