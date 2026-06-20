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
    void findAll_DevuelveAlertaPorAgroquimicoBajoStock() {
        factory.createAgroquimico("Sulfato Bajo Stock", 2.0, 10.0);

        ResponseEntity<AlertaResponse[]> response = restTemplate.getForEntity("/alertas", AlertaResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).anySatisfy(a -> assertThat(a.getTipo()).isEqualTo("AGROQUIMICO_BAJO_STOCK"));
    }

    @Test
    void findAll_ConParametrosPersonalizados_DevuelveOk() {
        ResponseEntity<AlertaResponse[]> response = restTemplate.getForEntity("/alertas?diasCultivo=15&diasRiego=3", AlertaResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
