package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.controller.support.TestDataFactory;
import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;
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
class CultivoControllerTest {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    @Autowired
    private TestRestTemplate restTemplate;

    private TestDataFactory factory;
    private Produccion produccion;
    private TipoCultivo tipoCultivo;

    @BeforeEach
    void setUp() {
        String suffix = String.valueOf(COUNTER.incrementAndGet());
        factory = new TestDataFactory(restTemplate);
        produccion = factory.createProduccion("Produccion Cultivo Test " + suffix);
        tipoCultivo = factory.createTipoCultivo("Tipo Cultivo Test " + suffix);
    }

    @Test
    void create_ConProduccionYTipoCultivoExistentes_CreaCultivo() {
        Cultivo nuevo = Cultivo.builder().descripcion("Mango").produccion(produccion).tipoCultivo(tipoCultivo).build();

        ResponseEntity<Cultivo> response = restTemplate.postForEntity("/cultivo/create", nuevo, Cultivo.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getProduccion().getId()).isEqualTo(produccion.getId());
        assertThat(response.getBody().getTipoCultivo().getId()).isEqualTo(tipoCultivo.getId());
    }

    @Test
    void create_SinProduccion_DevuelveBadRequest() {
        Cultivo invalido = Cultivo.builder().descripcion("Pina").tipoCultivo(tipoCultivo).build();

        ResponseEntity<String> response = restTemplate.postForEntity("/cultivo/create", invalido, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void create_ConProduccionInexistente_DevuelveBadRequest() {
        Produccion produccionFantasma = Produccion.builder().id(999999L).descripcion("X").build();
        Cultivo invalido = Cultivo.builder().descripcion("Aguacate").produccion(produccionFantasma).tipoCultivo(tipoCultivo).build();

        ResponseEntity<String> response = restTemplate.postForEntity("/cultivo/create", invalido, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void findByProduccion_DevuelveCultivosAsociados() {
        restTemplate.postForObject("/cultivo/create",
                Cultivo.builder().descripcion("Naranja").produccion(produccion).tipoCultivo(tipoCultivo).build(), Cultivo.class);

        ResponseEntity<Cultivo[]> response = restTemplate.getForEntity("/cultivo/findByProduccion/" + produccion.getId(), Cultivo[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void search_PorDescripcionParcial_EncuentraCoincidencias() {
        restTemplate.postForObject("/cultivo/create",
                Cultivo.builder().descripcion("Platano Enano").produccion(produccion).tipoCultivo(tipoCultivo).build(), Cultivo.class);

        ResponseEntity<Cultivo[]> response = restTemplate.getForEntity("/cultivo/search/platano", Cultivo[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).anySatisfy(c -> assertThat(c.getDescripcion()).containsIgnoringCase("platano"));
    }

    @Test
    void delete_CultivoExistente_LoElimina() {
        Cultivo creado = restTemplate.postForObject("/cultivo/create",
                Cultivo.builder().descripcion("Yuca").produccion(produccion).tipoCultivo(tipoCultivo).build(), Cultivo.class);

        restTemplate.delete("/cultivo/delete/" + creado.getId());

        ResponseEntity<Cultivo> response = restTemplate.getForEntity("/cultivo/findById/" + creado.getId(), Cultivo.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
    }
}
