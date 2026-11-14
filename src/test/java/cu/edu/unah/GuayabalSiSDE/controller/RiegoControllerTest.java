package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.controller.support.TestDataFactory;
import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponsePK;
import cu.edu.unah.GuayabalSiSDE.util.AreaResponse;
import cu.edu.unah.GuayabalSiSDE.util.RiegoResponse;
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
class RiegoControllerTest {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    @Autowired
    private TestRestTemplate restTemplate;

    private TestDataFactory factory;
    private AreaCultivoResponsePK pk;
    private Long areaId;

    @BeforeEach
    void setUp() {
        String suffix = String.valueOf(COUNTER.incrementAndGet());
        factory = new TestDataFactory(restTemplate);
        AreaResponse area = factory.createArea("Area Riego Test " + suffix, "Capa Riego");
        Produccion produccion = factory.createProduccion("Produccion Riego Test " + suffix);
        TipoCultivo tipoCultivo = factory.createTipoCultivo("Tipo Riego Test " + suffix);
        Cultivo cultivo = factory.createCultivo("Cultivo Riego Test " + suffix, produccion, tipoCultivo);
        factory.createAreaCultivo(area.getId(), cultivo.getId(), "20260201", "20260801");
        areaId = area.getId();
        pk = AreaCultivoResponsePK.builder().areaId(area.getId()).cultivoId(cultivo.getId()).fechaSiembra("20260201").build();
    }

    private RiegoResponse nuevoRiego(String fechaPlanificacion, String fechaReal) {
        return RiegoResponse.builder()
                .areaCultivoResponsePk(pk)
                .fechaPlanificacion(fechaPlanificacion)
                .fechaReal(fechaReal)
                .build();
    }

    @Test
    void create_ConAreaCultivoExistente_CreaRiego() {
        ResponseEntity<RiegoResponse> response = restTemplate.postForEntity("/riego/create",
                nuevoRiego("20260210", "20260210"), RiegoResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getAreaCultivoResponsePk().getAreaId()).isEqualTo(areaId);
    }

    @Test
    void create_ConAreaCultivoInexistente_DevuelveOkConCuerpoNulo() {
        AreaCultivoResponsePK pkInexistente = AreaCultivoResponsePK.builder()
                .areaId(999999L).cultivoId(999999L).fechaSiembra("20260201").build();
        RiegoResponse invalido = RiegoResponse.builder()
                .areaCultivoResponsePk(pkInexistente)
                .fechaPlanificacion("20260210")
                .fechaReal("20260210")
                .build();

        ResponseEntity<RiegoResponse> response = restTemplate.postForEntity("/riego/create", invalido, RiegoResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void findById_RiegoExistente_LoEncuentra() {
        RiegoResponse creado = restTemplate.postForObject("/riego/create", nuevoRiego("20260211", "20260211"), RiegoResponse.class);

        ResponseEntity<RiegoResponse> response = restTemplate.getForEntity("/riego/findById/" + creado.getId(), RiegoResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getFechaPlanificacion()).isEqualTo("20260211");
    }

    @Test
    void findByAreaCultivoPk_DevuelveRiegosDeEseAreaCultivo() {
        restTemplate.postForObject("/riego/create", nuevoRiego("20260212", "20260212"), RiegoResponse.class);

        ResponseEntity<RiegoResponse[]> response = restTemplate.postForEntity("/riego/findByAreaCultivoPk", pk, RiegoResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void edit_ActualizaFechaReal() {
        RiegoResponse creado = restTemplate.postForObject("/riego/create", nuevoRiego("20260213", "20260213"), RiegoResponse.class);
        creado.setFechaReal("20260214");

        ResponseEntity<RiegoResponse> response = restTemplate.exchange("/riego/edit",
                org.springframework.http.HttpMethod.PUT, new org.springframework.http.HttpEntity<>(creado), RiegoResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getFechaReal()).isEqualTo("20260214");
    }

    @Test
    void findRiegosProximos_DevuelveListaOk() {
        ResponseEntity<RiegoResponse[]> response = restTemplate.getForEntity("/riego/proximos/7", RiegoResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void findHistorialByArea_DevuelveListaOk() {
        restTemplate.postForObject("/riego/create", nuevoRiego("20260215", "20260215"), RiegoResponse.class);

        ResponseEntity<RiegoResponse[]> response = restTemplate.getForEntity("/riego/historial/area/" + areaId, RiegoResponse[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void delete_RiegoExistente_LoElimina() {
        RiegoResponse creado = restTemplate.postForObject("/riego/create", nuevoRiego("20260216", "20260216"), RiegoResponse.class);

        restTemplate.delete("/riego/delete/" + creado.getId());

        ResponseEntity<RiegoResponse> response = restTemplate.getForEntity("/riego/findById/" + creado.getId(), RiegoResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }
}
