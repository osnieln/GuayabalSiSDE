package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.controller.support.TestDataFactory;
import cu.edu.unah.GuayabalSiSDE.util.AgroquimicoResponse;
import cu.edu.unah.GuayabalSiSDE.util.MovimientoAgroquimicoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovimientoAgroquimicoControllerTest {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    @Autowired
    private TestRestTemplate restTemplate;

    private TestDataFactory factory;
    private AgroquimicoResponse agroquimico;

    @BeforeEach
    void setUp() {
        factory = new TestDataFactory(restTemplate);
        agroquimico = factory.createAgroquimico("Nitrato " + COUNTER.incrementAndGet(), 20.0, 5.0);
    }

    private MovimientoAgroquimicoResponse nuevoMovimiento(String tipo, double cantidad) {
        return MovimientoAgroquimicoResponse.builder()
                .agroquimicoId(agroquimico.getId())
                .tipo(tipo)
                .cantidad(cantidad)
                .fecha("20260301")
                .build();
    }

    @Test
    void create_Entrada_IncrementaStock() {
        ResponseEntity<MovimientoAgroquimicoResponse> response = restTemplate.postForEntity("/movimientoAgroquimico/create",
                nuevoMovimiento("ENTRADA", 10.0), MovimientoAgroquimicoResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isNotNull();

        ResponseEntity<AgroquimicoResponse> agroquimicoActualizado = restTemplate.getForEntity(
                "/agroquimico/findById/" + agroquimico.getId(), AgroquimicoResponse.class);
        assertThat(agroquimicoActualizado.getBody().getStockActual()).isEqualTo(30.0);
    }

    @Test
    void create_SalidaConStockSuficiente_DecrementaStock() {
        ResponseEntity<MovimientoAgroquimicoResponse> response = restTemplate.postForEntity("/movimientoAgroquimico/create",
                nuevoMovimiento("SALIDA", 5.0), MovimientoAgroquimicoResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<AgroquimicoResponse> agroquimicoActualizado = restTemplate.getForEntity(
                "/agroquimico/findById/" + agroquimico.getId(), AgroquimicoResponse.class);
        assertThat(agroquimicoActualizado.getBody().getStockActual()).isEqualTo(15.0);
    }

    @Test
    void create_SalidaConStockInsuficiente_DevuelveBadRequest() {
        ResponseEntity<String> response = restTemplate.postForEntity("/movimientoAgroquimico/create",
                nuevoMovimiento("SALIDA", 1000.0), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void create_CantidadCero_DevuelveBadRequest() {
        ResponseEntity<String> response = restTemplate.postForEntity("/movimientoAgroquimico/create",
                nuevoMovimiento("ENTRADA", 0.0), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void findByAgroquimico_DevuelveMovimientosAsociados() {
        restTemplate.postForObject("/movimientoAgroquimico/create", nuevoMovimiento("ENTRADA", 3.0), MovimientoAgroquimicoResponse.class);

        ResponseEntity<MovimientoAgroquimicoResponse[]> response = restTemplate.getForEntity(
                "/movimientoAgroquimico/agroquimico/" + agroquimico.getId(), MovimientoAgroquimicoResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void delete_MovimientoExistente_RevierteStock() {
        MovimientoAgroquimicoResponse creado = restTemplate.postForObject("/movimientoAgroquimico/create",
                nuevoMovimiento("SALIDA", 4.0), MovimientoAgroquimicoResponse.class);

        restTemplate.delete("/movimientoAgroquimico/delete/" + creado.getId());

        ResponseEntity<AgroquimicoResponse> agroquimicoActualizado = restTemplate.getForEntity(
                "/agroquimico/findById/" + agroquimico.getId(), AgroquimicoResponse.class);
        assertThat(agroquimicoActualizado.getBody().getStockActual()).isEqualTo(20.0);
    }
}
