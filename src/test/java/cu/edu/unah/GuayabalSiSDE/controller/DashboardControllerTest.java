package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.controller.support.TestDataFactory;
import cu.edu.unah.GuayabalSiSDE.util.DashboardResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DashboardControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getDashboard_DevuelveAgregadosConGraficas() {
        TestDataFactory factory = new TestDataFactory(restTemplate);
        factory.createArea("Area Dashboard Test", "Capa Dashboard");

        ResponseEntity<DashboardResponse> response = restTemplate.getForEntity("/dashboard", DashboardResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTotalAreas()).isGreaterThanOrEqualTo(1);
        assertThat(response.getBody().getProduccionMensual()).isNotNull();
        assertThat(response.getBody().getRiegosMensual()).isNotNull();
        assertThat(response.getBody().getDistribucionCultivos()).isNotNull();
    }
}
