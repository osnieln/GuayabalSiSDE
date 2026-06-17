package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.Agroquimico;
import cu.edu.unah.GuayabalSiSDE.entity.Area;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.services.AgroquimicoService;
import cu.edu.unah.GuayabalSiSDE.services.AreaCultivoService;
import cu.edu.unah.GuayabalSiSDE.services.RiegoService;
import cu.edu.unah.GuayabalSiSDE.util.AlertaResponse;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponsePK;
import cu.edu.unah.GuayabalSiSDE.util.DateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "alertas")
public class AlertaController {

    private static final int DEFAULT_DIAS = 7;

    @Autowired
    private AreaCultivoService areaCultivoService;

    @Autowired
    private RiegoService riegoService;

    @Autowired
    private AgroquimicoService agroquimicoService;

    @GetMapping
    public ResponseEntity<List<AlertaResponse>> findAll(
            @RequestParam(required = false, defaultValue = "" + DEFAULT_DIAS) int diasCultivo,
            @RequestParam(required = false, defaultValue = "" + DEFAULT_DIAS) int diasRiego) {

        List<AlertaResponse> alertas = new ArrayList<>();

        for (AreaCultivo ac : areaCultivoService.findCultivosPorVencer(diasCultivo)) {
            alertas.add(AlertaResponse.builder()
                    .tipo("CULTIVO_POR_VENCER")
                    .prioridad("ALTA")
                    .mensaje("El cultivo " + nombreCultivo(ac) + " en el área " + nombreArea(ac)
                            + " está próximo a su fecha de recogida.")
                    .fechaReferencia(ac.getFechaRecogida() != null ? DateFormatter.format(ac.getFechaRecogida()) : null)
                    .areaCultivoResponsePk(AreaCultivoResponsePK.map(ac.getAreaCultivoPk()))
                    .build());
        }

        for (AreaCultivo ac : riegoService.findAreasSinRiego(diasRiego)) {
            alertas.add(AlertaResponse.builder()
                    .tipo("RIEGO_PENDIENTE")
                    .prioridad("MEDIA")
                    .mensaje("El área " + nombreArea(ac) + " (cultivo " + nombreCultivo(ac)
                            + ") lleva más de " + diasRiego + " días sin riego registrado.")
                    .areaCultivoResponsePk(AreaCultivoResponsePK.map(ac.getAreaCultivoPk()))
                    .build());
        }

        for (Agroquimico a : agroquimicoService.findBajoStock()) {
            alertas.add(AlertaResponse.builder()
                    .tipo("AGROQUIMICO_BAJO_STOCK")
                    .prioridad("MEDIA")
                    .mensaje("El agroquímico \"" + a.getNombre() + "\" tiene existencia baja ("
                            + a.getStockActual() + " de mínimo " + a.getStockMinimo() + ").")
                    .agroquimicoId(a.getId())
                    .build());
        }

        return ResponseEntity.ok(alertas);
    }

    private String nombreCultivo(AreaCultivo ac) {
        Cultivo c = ac.getCultivo();
        return c != null && c.getDescripcion() != null ? c.getDescripcion() : "N/D";
    }

    private String nombreArea(AreaCultivo ac) {
        Area a = ac.getArea();
        return a != null && a.getDescripcion() != null ? a.getDescripcion() : "N/D";
    }
}
