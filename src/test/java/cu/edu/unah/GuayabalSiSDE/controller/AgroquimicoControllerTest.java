package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.controller.support.TestDataFactory;
import cu.edu.unah.GuayabalSiSDE.util.AgroquimicoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AgroquimicoControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private TestDataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TestDataFactory(restTemplate);
    }

    @Test
    void create_DatosValidos_CreaAgroquimico() {
        AgroquimicoResponse creado = factory.createAgroquimico("Glifosato", 50.0, 10.0);

        assertThat(creado.getId()).isNotNull();
        assertThat(creado.getNombre()).isEqualTo("Glifosato");
        assertThat(creado.getStockActual()).isEqualTo(50.0);
    }

    @Test
    void create_NombreDuplicado_DevuelveBadRequest() {
        factory.createAgroquimico("Urea", 20.0, 5.0);

        ResponseEntity<String> response = restTemplate.postForEntity("/agroquimico/create",
                AgroquimicoResponse.builder().nombre("Urea").stockActual(1.0).stockMinimo(1.0)
                        .areaCultivoResponsePKListList(java.util.Collections.emptyList()).build(),
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void findById_AgroquimicoExistente_LoEncuentra() {
        AgroquimicoResponse creado = factory.createAgroquimico("Fosfato", 30.0, 5.0);

        ResponseEntity<AgroquimicoResponse> response = restTemplate.getForEntity("/agroquimico/findById/" + creado.getId(), AgroquimicoResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getNombre()).isEqualTo("Fosfato");
    }

    @Test
    void edit_ActualizaStock() {
        AgroquimicoResponse creado = factory.createAgroquimico("Potasio", 15.0, 5.0);
        creado.setStockActual(25.0);

        ResponseEntity<AgroquimicoResponse> response = restTemplate.exchange("/agroquimico/edit",
                org.springframework.http.HttpMethod.PUT, new org.springframework.http.HttpEntity<>(creado), AgroquimicoResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getStockActual()).isEqualTo(25.0);
    }

    @Test
    void delete_SinAsociaciones_LoElimina() {
        AgroquimicoResponse creado = factory.createAgroquimico("Calcio", 5.0, 1.0);

        restTemplate.delete("/agroquimico/delete/" + creado.getId());

        ResponseEntity<AgroquimicoResponse> response = restTemplate.getForEntity("/agroquimico/findById/" + creado.getId(), AgroquimicoResponse.class);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void masUtilizados_DevuelveListaConCantidadDeUsos() {
        factory.createAgroquimico("Azufre", 8.0, 2.0);

        ResponseEntity<Object[]> response = restTemplate.getForEntity("/agroquimico/masUtilizados", Object[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
