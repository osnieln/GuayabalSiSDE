package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.controller.support.TestDataFactory;
import cu.edu.unah.GuayabalSiSDE.util.TrabajadorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TrabajadorControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private TestDataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TestDataFactory(restTemplate);
    }

    @Test
    void create_DatosValidos_CreaTrabajador() {
        TrabajadorResponse creado = factory.createTrabajador("Juan Perez", "12345678901", "Agricultor");

        assertThat(creado.getId()).isNotNull();
        assertThat(creado.getNombre()).isEqualTo("Juan Perez");
        assertThat(creado.getCargo()).isEqualTo("Agricultor");
    }

    @Test
    void create_IdentificacionInvalida_DevuelveBadRequest() {
        ResponseEntity<String> response = restTemplate.postForEntity("/trabajador/create",
                TrabajadorResponse.builder().nombre("Pedro").identificacion("123").cargo("Agricultor")
                        .activo(true).areaIds(new java.util.ArrayList<>()).riegoIds(new java.util.ArrayList<>()).build(),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void create_CargoVacio_DevuelveBadRequest() {
        ResponseEntity<String> response = restTemplate.postForEntity("/trabajador/create",
                TrabajadorResponse.builder().nombre("Pedro").identificacion("12345678902").cargo("")
                        .activo(true).areaIds(new java.util.ArrayList<>()).riegoIds(new java.util.ArrayList<>()).build(),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void findById_TrabajadorExistente_LoEncuentra() {
        TrabajadorResponse creado = factory.createTrabajador("Maria Lopez", "22345678901", "Supervisor");

        ResponseEntity<TrabajadorResponse> response = restTemplate.getForEntity("/trabajador/findById/" + creado.getId(), TrabajadorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getNombre()).isEqualTo("Maria Lopez");
    }

    @Test
    void edit_ActualizaCargo() {
        TrabajadorResponse creado = factory.createTrabajador("Luis Diaz", "32345678901", "Regador");
        creado.setCargo("Supervisor");

        ResponseEntity<TrabajadorResponse> response = restTemplate.exchange("/trabajador/edit",
                HttpMethod.PUT, new HttpEntity<>(creado), TrabajadorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getCargo()).isEqualTo("Supervisor");
    }

    @Test
    void delete_TrabajadorExistente_LoElimina() {
        TrabajadorResponse creado = factory.createTrabajador("Ana Cruz", "42345678901", "Agricultor");

        restTemplate.delete("/trabajador/delete/" + creado.getId());

        ResponseEntity<TrabajadorResponse> response = restTemplate.getForEntity("/trabajador/findById/" + creado.getId(), TrabajadorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }
}
