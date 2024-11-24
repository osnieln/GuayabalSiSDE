package cu.edu.unah.GuayabalSiSDE.repository;


import cu.edu.unah.GuayabalSiSDE.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

}
