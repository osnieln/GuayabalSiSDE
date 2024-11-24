package cu.edu.unah.GuayabalSiSDE.repository;

import cu.edu.unah.GuayabalSiSDE.entity.Authorities;
import cu.edu.unah.GuayabalSiSDE.entity.AuthoritiesPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, AuthoritiesPK> {

	@Query(value = "SELECT o FROM Authorities as o WHERE o.authoritiesPK.username like :username")
	List<Authorities> findByUsername(String username);
}
