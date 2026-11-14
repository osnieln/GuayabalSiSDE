package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.controller.support.TestDataFactory;
import cu.edu.unah.GuayabalSiSDE.util.AlertaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AlertaControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private TestDataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TestDataFactory(restTemplate);
    }

    @Test
    void generar_PersisteAlertaPorAgroquimicoBajoStock() {
        factory.createAgroquimico("Sulfato Bajo Stock " + System.nanoTime(), 2.0, 10.0);

        restTemplate.postForEntity("/alertas/generar", null, Void.class);
        ResponseEntity<AlertaResponse[]> response = restTemplate.getForEntity("/alertas", AlertaResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).anySatisfy(a -> assertThat(a.getTipo()).isEqualTo("AGROQUIMICO_BAJO_STOCK"));
    }

    @Test
    void findAll_ConSoloNoLeidas_DevuelveOk() {
        ResponseEntity<AlertaResponse[]> response = restTemplate.getForEntity("/alertas?soloNoLeidas=true", AlertaResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void marcarLeidaYResuelta_ActualizanLaAlerta() {
        factory.createAgroquimico("Potasio Bajo Stock " + System.nanoTime(), 1.0, 5.0);
        restTemplate.postForEntity("/alertas/generar", null, Void.class);

        AlertaResponse[] alertas = restTemplate.getForObject("/alertas", AlertaResponse[].class);
        assertThat(alertas).isNotEmpty();
        Long id = alertas[0].getId();

        ResponseEntity<AlertaResponse> leida = restTemplate.exchange(
                "/alertas/" + id + "/leida", org.springframework.http.HttpMethod.PUT, null, AlertaResponse.class);
        assertThat(leida.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(leida.getBody().isLeida()).isTrue();

        ResponseEntity<AlertaResponse> resuelta = restTemplate.exchange(
                "/alertas/" + id + "/resuelta", org.springframework.http.HttpMethod.PUT, null, AlertaResponse.class);
        assertThat(resuelta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resuelta.getBody().isResuelta()).isTrue();
    }
}
