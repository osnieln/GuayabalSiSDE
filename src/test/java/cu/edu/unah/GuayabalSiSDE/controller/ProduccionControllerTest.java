package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProduccionControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void create_DatosValidos_DevuelveProduccionConId() {
        Produccion nueva = Produccion.builder().descripcion("Exportación").build();

        ResponseEntity<Produccion> response = restTemplate.postForEntity("/produccion/create", nueva, Produccion.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getDescripcion()).isEqualTo("Exportación");
    }

    @Test
    void create_DescripcionVacia_DevuelveBadRequest() {
        Produccion invalida = Produccion.builder().descripcion("").build();

        ResponseEntity<String> response = restTemplate.postForEntity("/produccion/create", invalida, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void edit_ProduccionExistente_ActualizaDescripcion() {
        Produccion creada = restTemplate.postForObject("/produccion/create",
                Produccion.builder().descripcion("Consumo Nacional").build(), Produccion.class);
        creada.setDescripcion("Consumo Nacional Editado");

        ResponseEntity<Produccion> response = restTemplate.exchange("/produccion/edit",
                org.springframework.http.HttpMethod.PUT,
                new org.springframework.http.HttpEntity<>(creada), Produccion.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getDescripcion()).isEqualTo("Consumo Nacional Editado");
    }

    @Test
    void delete_ProduccionInexistente_DevuelveBadRequest() {
        ResponseEntity<String> response = restTemplate.exchange("/produccion/delete/999999",
                org.springframework.http.HttpMethod.DELETE, null, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void findAll_DevuelveListaConElementoCreado() {
        restTemplate.postForObject("/produccion/create", Produccion.builder().descripcion("Semilla").build(), Produccion.class);

        ResponseEntity<Produccion[]> response = restTemplate.getForEntity("/produccion", Produccion[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }
}
