package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Authorities;
import cu.edu.unah.GuayabalSiSDE.entity.AuthoritiesPK;
import cu.edu.unah.GuayabalSiSDE.repository.AuthoritiesRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthoritiesServices {

	@Autowired
	private AuthoritiesRepository authoritiesRepository;
	
	public List<Authorities> findAll() {
		return authoritiesRepository.findAll();
	}

	public Authorities findById(AuthoritiesPK id) {
		return authoritiesRepository.findById(id).get();
	}
	public List<Authorities> findByUsername(String username){
		System.out.println(authoritiesRepository.findByUsername(username));
		return authoritiesRepository.findByUsername(username);
	}

	public Authorities save(Authorities authorities) {
		if (authorities.getAuthoritiesPK()!=null && authoritiesRepository.existsById(authorities.getAuthoritiesPK())) {
			throw new EntityExistsException("There is already existing entity with such ID in the database.");
		}

		return authoritiesRepository.save(authorities);
	}

	public Authorities update(Authorities authorities) {
		if (authorities.getAuthoritiesPK()!=null && !authoritiesRepository.existsById(authorities.getAuthoritiesPK())) {
			throw new EntityNotFoundException("There is no entity with such ID in the database.");
		}

		return authoritiesRepository.save(authorities);
	}

	public void delete(AuthoritiesPK id) {
		authoritiesRepository.deleteById(id);
	}
}
