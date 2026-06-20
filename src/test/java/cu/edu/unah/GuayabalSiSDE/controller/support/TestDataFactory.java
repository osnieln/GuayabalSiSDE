package cu.edu.unah.GuayabalSiSDE.controller.support;

import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;
import cu.edu.unah.GuayabalSiSDE.util.AgroquimicoResponse;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponse;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponsePK;
import cu.edu.unah.GuayabalSiSDE.util.AreaResponse;
import cu.edu.unah.GuayabalSiSDE.util.TrabajadorResponse;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Crea entidades prerrequisito a través de los propios endpoints REST (no de los repositorios),
 * para que las pruebas de integración ejerciten siempre la pila completa HTTP -> controlador -> servicio -> JPA.
 */
public class TestDataFactory {

    private static final String POLYGON_WKT = "POLYGON((-84.5 15.5, -84.5 15.6, -84.4 15.6, -84.4 15.5, -84.5 15.5))";

    private final TestRestTemplate restTemplate;

    public TestDataFactory(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Produccion createProduccion(String descripcion) {
        Produccion nuevo = Produccion.builder().descripcion(descripcion).build();
        return restTemplate.postForObject("/produccion/create", nuevo, Produccion.class);
    }

    public TipoCultivo createTipoCultivo(String nombre) {
        TipoCultivo nuevo = TipoCultivo.builder().nombre(nombre).build();
        return restTemplate.postForObject("/tipoCultivo/create", nuevo, TipoCultivo.class);
    }

    public Cultivo createCultivo(String descripcion, Produccion produccion, TipoCultivo tipoCultivo) {
        Cultivo nuevo = Cultivo.builder()
                .descripcion(descripcion)
                .produccion(produccion)
                .tipoCultivo(tipoCultivo)
                .build();
        return restTemplate.postForObject("/cultivo/create", nuevo, Cultivo.class);
    }

    public AreaResponse createArea(String descripcion, String capa) {
        AreaResponse nuevo = AreaResponse.builder()
                .descripcion(descripcion)
                .capa(capa)
                .ubicacion(POLYGON_WKT)
                .build();
        return restTemplate.postForObject("/area/create", nuevo, AreaResponse.class);
    }

    public AreaCultivoResponse createAreaCultivo(Long areaId, Long cultivoId, String fechaSiembra, String fechaRecogida) {
        AreaCultivoResponse nuevo = AreaCultivoResponse.builder()
                .areaCultivoResponsePK(AreaCultivoResponsePK.builder()
                        .areaId(areaId)
                        .cultivoId(cultivoId)
                        .fechaSiembra(fechaSiembra)
                        .build())
                .fechaRecogida(fechaRecogida)
                .planProd(1000L)
                .prodCultivosPermanente(0.0)
                .prodCultivosTemporales(0.0)
                .produccionReal(0.0)
                .activo(true)
                .agroquimicos(Collections.emptyList())
                .build();
        return restTemplate.postForObject("/areaCultivo/create", nuevo, AreaCultivoResponse.class);
    }

    public AgroquimicoResponse createAgroquimico(String nombre, double stockActual, double stockMinimo) {
        AgroquimicoResponse nuevo = AgroquimicoResponse.builder()
                .nombre(nombre)
                .stockActual(stockActual)
                .stockMinimo(stockMinimo)
                .areaCultivoResponsePKListList(new ArrayList<>())
                .build();
        return restTemplate.postForObject("/agroquimico/create", nuevo, AgroquimicoResponse.class);
    }

    public TrabajadorResponse createTrabajador(String nombre, String identificacion11Digitos, String cargo) {
        TrabajadorResponse nuevo = TrabajadorResponse.builder()
                .nombre(nombre)
                .identificacion(identificacion11Digitos)
                .cargo(cargo)
                .activo(true)
                .areaIds(new ArrayList<>())
                .riegoIds(new ArrayList<>())
                .build();
        return restTemplate.postForObject("/trabajador/create", nuevo, TrabajadorResponse.class);
    }
}
