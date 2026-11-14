package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.controller.support.TestDataFactory;
import cu.edu.unah.GuayabalSiSDE.util.AreaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AreaControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private TestDataFactory factory;

    @BeforeEach
    void setUp() {
        factory = new TestDataFactory(restTemplate);
    }

    @Test
    void create_ConGeometriaValida_PersisteYDevuelvePoligono() {
        AreaResponse creada = factory.createArea("Lote 1", "Capa Norte");

        assertThat(creada.getId()).isNotNull();
        assertThat(creada.getUbicacion()).containsIgnoringCase("POLYGON");
        assertThat(creada.getCapa()).isEqualTo("Capa Norte");
    }

    @Test
    void findById_AreaExistente_DevuelvePoligonoEquivalente() {
        AreaResponse creada = factory.createArea("Lote 2", "Capa Sur");

        ResponseEntity<AreaResponse> response = restTemplate.getForEntity("/area/findById/" + creada.getId(), AreaResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getUbicacion()).containsIgnoringCase("POLYGON");
    }

    @Test
    void findDistinctCapa_DevuelveCapaCreada() {
        factory.createArea("Lote 3", "Capa Unica XYZ");

        ResponseEntity<String[]> response = restTemplate.getForEntity("/area/findDistinctCapa", String[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Capa Unica XYZ");
    }

    @Test
    void findByCapa_FiltraPorCapa() {
        factory.createArea("Lote 4", "CapaFiltrable");

        ResponseEntity<AreaResponse[]> response = restTemplate.getForEntity("/area/findByCapa/CapaFiltrable", AreaResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).allSatisfy(a -> assertThat(a.getCapa()).isEqualTo("CapaFiltrable"));
    }

    @Test
    void exportCsv_DevuelveContenidoCsv() {
        factory.createArea("Lote 5", "Capa CSV");

        ResponseEntity<byte[]> response = restTemplate.getForEntity("/area/export/csv", byte[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String body = new String(response.getBody());
        assertThat(body).startsWith("ID,Descripcion,Capa");
    }

    @Test
    void delete_AreaExistente_LaElimina() {
        AreaResponse creada = factory.createArea("Lote 6", "Capa Borrable");

        restTemplate.delete("/area/delete/" + creada.getId());

        ResponseEntity<AreaResponse> response = restTemplate.getForEntity("/area/findById/" + creada.getId(), AreaResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }
}
