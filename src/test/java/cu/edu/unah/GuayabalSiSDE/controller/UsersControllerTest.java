package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private Users nuevoUsuario(String username) {
        return new Users(username, "12345678901", "Usuario de Prueba", username + "@example.com", "password123", true, "Descripcion");
    }

    @Test
    void create_DatosValidos_CreaUsuario() {
        ResponseEntity<Users> response = restTemplate.postForEntity("/Users/create", nuevoUsuario("jperez1"), Users.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getUsername()).isEqualTo("jperez1");
    }

    @Test
    void create_UsuarioDuplicado_DevuelveError500() {
        restTemplate.postForEntity("/Users/create", nuevoUsuario("jperez2"), Users.class);

        ResponseEntity<String> response = restTemplate.postForEntity("/Users/create", nuevoUsuario("jperez2"), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void findById_UsuarioExistente_LoEncuentra() {
        restTemplate.postForEntity("/Users/create", nuevoUsuario("jperez3"), Users.class);

        ResponseEntity<Users> response = restTemplate.getForEntity("/Users/find/jperez3", Users.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getUsername()).isEqualTo("jperez3");
    }

    @Test
    void findById_UsuarioInexistente_DevuelveNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/Users/find/noexiste1", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void update_UsuarioExistente_ActualizaNombre() {
        Users creado = restTemplate.postForObject("/Users/create", nuevoUsuario("jperez4"), Users.class);
        creado.setNombre("Nombre Actualizado");

        ResponseEntity<Users> response = restTemplate.exchange("/Users/update",
                HttpMethod.PUT, new org.springframework.http.HttpEntity<>(creado), Users.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getNombre()).isEqualTo("Nombre Actualizado");
    }

    @Test
    void update_UsuarioInexistente_DevuelveNotFound() {
        Users fantasma = nuevoUsuario("noexiste2");

        ResponseEntity<String> response = restTemplate.exchange("/Users/update",
                HttpMethod.PUT, new org.springframework.http.HttpEntity<>(fantasma), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void delete_UsuarioExistente_LoElimina() {
        restTemplate.postForEntity("/Users/create", nuevoUsuario("jperez5"), Users.class);

        restTemplate.delete("/Users/delete/jperez5");

        ResponseEntity<String> response = restTemplate.getForEntity("/Users/find/jperez5", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAuthoritiesByUsername_SinAuthorities_DevuelveListaVacia() {
        restTemplate.postForEntity("/Users/create", nuevoUsuario("jperez6"), Users.class);

        ResponseEntity<String[]> response = restTemplate.getForEntity("/Users/findAuthoritiesByUsername/jperez6", String[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }
}
