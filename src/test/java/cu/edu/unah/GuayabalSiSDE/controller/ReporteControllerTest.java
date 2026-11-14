package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.controller.support.TestDataFactory;
import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;
import cu.edu.unah.GuayabalSiSDE.util.AreaResponse;
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
class ReporteControllerTest {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        String suffix = String.valueOf(COUNTER.incrementAndGet());
        TestDataFactory factory = new TestDataFactory(restTemplate);
        AreaResponse area = factory.createArea("Area Reporte Test " + suffix, "Capa Reporte");
        Produccion produccion = factory.createProduccion("Produccion Reporte Test " + suffix);
        TipoCultivo tipoCultivo = factory.createTipoCultivo("Tipo Reporte Test " + suffix);
        Cultivo cultivo = factory.createCultivo("Cultivo Reporte Test " + suffix, produccion, tipoCultivo);
        factory.createAreaCultivo(area.getId(), cultivo.getId(), "20260301", "20260901");
    }

    @Test
    void previewTodasAreasCultivo_DevuelveListaJson() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity("/api/reportes/preview/todasAreasCultivo", Object[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void previewPlanProdBetween_DevuelveListaJson() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity("/api/reportes/preview/planProdBetween/0/100000", Object[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void previewProdCultivosPermanenteAfter_DevuelveListaJson() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity("/api/reportes/preview/prodCultivosPermanenteAfter/0", Object[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void previewFechaRecogidaBefore_ConFechaValida_DevuelveListaJson() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity("/api/reportes/preview/fechaRecogidaBefore/01-12-2026", Object[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void previewFechaRecogidaBefore_ConFechaInvalida_DevuelveBadRequest() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/reportes/preview/fechaRecogidaBefore/no-es-una-fecha", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void previewCultivosPorVencer_DevuelveListaJson() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity("/api/reportes/preview/cultivosPorVencer/365", Object[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void previewAgroquimicosMasUsados_DevuelveListaJson() {
        ResponseEntity<Object[]> response = restTemplate.getForEntity("/api/reportes/preview/agroquimicosMasUsados", Object[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void diagnostico_DevuelveTextoPlano() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/reportes/diagnostico", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("DIAGNÓSTICO DE REPORTES");
    }

    @Test
    void testPdf_DevuelvePdfValido() {
        ResponseEntity<byte[]> response = restTemplate.getForEntity("/api/reportes/testPdf", byte[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }
}
