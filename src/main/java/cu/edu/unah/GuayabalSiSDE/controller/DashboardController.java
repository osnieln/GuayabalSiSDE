package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.services.AlertaSistemaService;
import cu.edu.unah.GuayabalSiSDE.services.AreaCultivoService;
import cu.edu.unah.GuayabalSiSDE.services.AreaService;
import cu.edu.unah.GuayabalSiSDE.services.RiegoService;
import cu.edu.unah.GuayabalSiSDE.util.DashboardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "dashboard")
public class DashboardController {

    private static final int DIAS_RIEGO_PROXIMO = 7;
    private static final int MESES_HISTORICO = 6;

    @Autowired
    AreaService areaService;

    @Autowired
    AreaCultivoService areaCultivoService;

    @Autowired
    RiegoService riegoService;

    @Autowired
    AlertaSistemaService alertaSistemaService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard() {
        long totalAreas = areaService.count();
        long totalCultivosActivos = areaCultivoService.countActivos();
        long totalRiegosProximosSemana = riegoService.countRiegosProximos(DIAS_RIEGO_PROXIMO);
        long alertasActivas = alertaSistemaService.countActivas();

        int mesActual = LocalDate.now().getMonthValue();
        int anioActual = LocalDate.now().getYear();
        Double produccionMes = areaCultivoService.sumProduccionRealDelMes(mesActual, anioActual);

        return ResponseEntity.ok(DashboardResponse.builder()
                .totalAreas(totalAreas)
                .cultivosActivos(totalCultivosActivos)
                .riegosProximosSemana(totalRiegosProximosSemana)
                .produccionMesActual(produccionMes != null ? produccionMes : 0.0)
                .alertasActivas(alertasActivas)
                .produccionMensual(areaCultivoService.findProduccionMensual(MESES_HISTORICO))
                .riegosMensual(riegoService.findRiegosMensual(MESES_HISTORICO))
                .distribucionCultivos(areaCultivoService.findDistribucionCultivos())
                .build());
    }
}
