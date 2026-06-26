package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TipoCultivoControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void create_DatosValidos_DevuelveTipoCultivoConId() {
        TipoCultivo nuevo = TipoCultivo.builder().nombre("Hortaliza").build();

        ResponseEntity<TipoCultivo> response = restTemplate.postForEntity("/tipoCultivo/create", nuevo, TipoCultivo.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getNombre()).isEqualTo("Hortaliza");
    }

    @Test
    void create_NombreVacio_DevuelveBadRequest() {
        TipoCultivo invalido = TipoCultivo.builder().nombre("").build();

        ResponseEntity<String> response = restTemplate.postForEntity("/tipoCultivo/create", invalido, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void findById_TipoCultivoExistente_DevuelveTipoCultivo() {
        TipoCultivo creado = restTemplate.postForObject("/tipoCultivo/create",
                TipoCultivo.builder().nombre("Frutal").build(), TipoCultivo.class);

        ResponseEntity<TipoCultivo> response = restTemplate.getForEntity("/tipoCultivo/findById/" + creado.getId(), TipoCultivo.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getNombre()).isEqualTo("Frutal");
    }

    @Test
    void findAll_DevuelveListaConElementoCreado() {
        restTemplate.postForObject("/tipoCultivo/create", TipoCultivo.builder().nombre("Cereal").build(), TipoCultivo.class);

        ResponseEntity<TipoCultivo[]> response = restTemplate.getForEntity("/tipoCultivo", TipoCultivo[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void delete_TipoCultivoExistente_LoElimina() {
        TipoCultivo creado = restTemplate.postForObject("/tipoCultivo/create",
                TipoCultivo.builder().nombre("Tuberculo").build(), TipoCultivo.class);

        restTemplate.delete("/tipoCultivo/delete/" + creado.getId());

        ResponseEntity<TipoCultivo> response = restTemplate.getForEntity("/tipoCultivo/findById/" + creado.getId(), TipoCultivo.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }
}
