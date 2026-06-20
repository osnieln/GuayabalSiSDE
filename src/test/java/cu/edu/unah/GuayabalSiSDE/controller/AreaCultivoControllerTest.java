package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.controller.support.TestDataFactory;
import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponse;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponsePK;
import cu.edu.unah.GuayabalSiSDE.util.AreaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AreaCultivoControllerTest {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    @Autowired
    private TestRestTemplate restTemplate;

    private TestDataFactory factory;
    private AreaResponse area;
    private Cultivo cultivo;

    @BeforeEach
    void setUp() {
        String suffix = String.valueOf(COUNTER.incrementAndGet());
        factory = new TestDataFactory(restTemplate);
        area = factory.createArea("Area AC Test " + suffix, "Capa AC");
        Produccion produccion = factory.createProduccion("Produccion AC Test " + suffix);
        TipoCultivo tipoCultivo = factory.createTipoCultivo("Tipo AC Test " + suffix);
        cultivo = factory.createCultivo("Cultivo AC Test " + suffix, produccion, tipoCultivo);
    }

    @Test
    void create_ConAreaYCultivoExistentes_CreaAreaCultivo() {
        AreaCultivoResponse creado = factory.createAreaCultivo(area.getId(), cultivo.getId(), "20260101", "20260601");

        assertThat(creado).isNotNull();
        assertThat(creado.getAreaCultivoResponsePK().getAreaId()).isEqualTo(area.getId());
        assertThat(creado.getAreaCultivoResponsePK().getCultivoId()).isEqualTo(cultivo.getId());
        assertThat(creado.isActivo()).isTrue();
    }

    @Test
    void create_Duplicado_DevuelveBadRequest() {
        factory.createAreaCultivo(area.getId(), cultivo.getId(), "20260102", "20260602");

        AreaCultivoResponse duplicado = AreaCultivoResponse.builder()
                .areaCultivoResponsePK(AreaCultivoResponsePK.builder()
                        .areaId(area.getId()).cultivoId(cultivo.getId()).fechaSiembra("20260102").build())
                .fechaRecogida("20260603")
                .activo(true)
                .agroquimicos(java.util.Collections.emptyList())
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity("/areaCultivo/create", duplicado, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void findById_AreaCultivoExistente_LoEncuentra() {
        factory.createAreaCultivo(area.getId(), cultivo.getId(), "20260103", "20260603");

        AreaCultivoResponsePK pk = AreaCultivoResponsePK.builder()
                .areaId(area.getId()).cultivoId(cultivo.getId()).fechaSiembra("20260103").build();

        ResponseEntity<AreaCultivoResponse> response = restTemplate.postForEntity("/areaCultivo/findById", pk, AreaCultivoResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getAreaCultivoResponsePK().getFechaSiembra()).isEqualTo("20260103");
    }

    @Test
    void findByActivo_DevuelveSoloActivos() {
        factory.createAreaCultivo(area.getId(), cultivo.getId(), "20260104", "20260604");

        ResponseEntity<AreaCultivoResponse[]> response = restTemplate.getForEntity("/areaCultivo/findByActivo/true", AreaCultivoResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).allSatisfy(ac -> assertThat(ac.isActivo()).isTrue());
    }

    @Test
    void rendimiento_DevuelveCalculoPorRegistro() {
        factory.createAreaCultivo(area.getId(), cultivo.getId(), "20260105", "20260605");

        ResponseEntity<Object[]> response = restTemplate.getForEntity("/areaCultivo/rendimiento", Object[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void delete_AreaCultivoExistente_LoElimina() {
        AreaCultivoResponsePK pk = AreaCultivoResponsePK.builder()
                .areaId(area.getId()).cultivoId(cultivo.getId()).fechaSiembra("20260106").build();
        factory.createAreaCultivo(area.getId(), cultivo.getId(), "20260106", "20260606");

        ResponseEntity<AreaCultivoResponse> deleteResponse = restTemplate.postForEntity("/areaCultivo/delete", pk, AreaCultivoResponse.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<AreaCultivoResponse> findResponse = restTemplate.postForEntity("/areaCultivo/findById", pk, AreaCultivoResponse.class);
        assertThat(findResponse.getBody()).isNull();
    }
}
