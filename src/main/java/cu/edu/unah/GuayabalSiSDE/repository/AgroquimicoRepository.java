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

    @Query("SELECT a FROM Agroquimico a LEFT JOIN a.areaCultivos ac GROUP BY a ORDER BY COUNT(ac) DESC")
    List<Agroquimico> findMasUtilizados();

    @Query("SELECT a.id, a.nombre, COUNT(ac) FROM Agroquimico a LEFT JOIN a.areaCultivos ac GROUP BY a.id, a.nombre ORDER BY COUNT(ac) DESC")
    List<Object[]> findMasUtilizadosConConteo();
}
