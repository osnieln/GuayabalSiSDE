package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.Authorities;
import cu.edu.unah.GuayabalSiSDE.entity.AuthoritiesPK;
import cu.edu.unah.GuayabalSiSDE.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthoritiesControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Users usuario;

    @BeforeEach
    void setUp() {
        usuario = new Users("authuser1", "12345678901", "Usuario Authorities", "authuser1@example.com", "password123", true, "Descripcion");
        restTemplate.postForEntity("/Users/create", usuario, Users.class);
    }

    private Authorities nuevaAuthority(String authority) {
        Authorities authorities = new Authorities();
        authorities.setAuthoritiesPK(new AuthoritiesPK(usuario.getUsername(), authority));
        return authorities;
    }

    @Test
    void create_DatosValidos_CreaAuthority() {
        ResponseEntity<Authorities> response = restTemplate.postForEntity("/Authorities/create", nuevaAuthority("ROLE_ADMIN"), Authorities.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getAuthoritiesPK().getAuthority()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void create_AuthorityDuplicada_DevuelveError500() {
        restTemplate.postForEntity("/Authorities/create", nuevaAuthority("ROLE_USER"), Authorities.class);

        ResponseEntity<String> response = restTemplate.postForEntity("/Authorities/create", nuevaAuthority("ROLE_USER"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void findByUsername_DevuelveAuthoritiesDelUsuario() {
        restTemplate.postForEntity("/Authorities/create", nuevaAuthority("ROLE_OPERADOR"), Authorities.class);

        ResponseEntity<Authorities[]> response = restTemplate.getForEntity("/Authorities/findByUsername/" + usuario.getUsername(), Authorities[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void update_AuthorityInexistente_DevuelveNotFound() {
        Authorities fantasma = nuevaAuthority("ROLE_FANTASMA");

        ResponseEntity<String> response = restTemplate.exchange("/Authorities/update",
                HttpMethod.PUT, new HttpEntity<>(fantasma), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void delete_AuthorityExistente_LaElimina() {
        restTemplate.postForEntity("/Authorities/create", nuevaAuthority("ROLE_BORRABLE"), Authorities.class);
        AuthoritiesPK pk = new AuthoritiesPK(usuario.getUsername(), "ROLE_BORRABLE");

        restTemplate.postForEntity("/Authorities/delete", pk, Void.class);

        ResponseEntity<Authorities[]> response = restTemplate.getForEntity("/Authorities/findByUsername/" + usuario.getUsername(), Authorities[].class);
        assertThat(response.getBody()).noneMatch(a -> "ROLE_BORRABLE".equals(a.getAuthoritiesPK().getAuthority()));
    }

    @Test
    void findAll_DevuelveListaConElementoCreado() {
        restTemplate.postForEntity("/Authorities/create", nuevaAuthority("ROLE_LISTADO"), Authorities.class);

        ResponseEntity<Authorities[]> response = restTemplate.getForEntity("/Authorities/findAll", Authorities[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }
}
