package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Riego;
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
import java.util.List;

@RestController
@RequestMapping(value = "dashboard")
public class DashboardController {

    @Autowired
    AreaService areaService;

    @Autowired
    AreaCultivoService areaCultivoService;

    @Autowired
    RiegoService riegoService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard() {
        long totalAreas = areaService.findAll().size();

        List<AreaCultivo> cultivosActivos = areaCultivoService.findByActivo(true);
        long totalCultivosActivos = cultivosActivos.size();

        List<Riego> riegosProximos = riegoService.findRiegosProximos(7);
        long totalRiegosProximosSemana = riegosProximos.size();

        int mesActual = LocalDate.now().getMonthValue();
        int anioActual = LocalDate.now().getYear();
        double produccionMes = areaCultivoService.findAll().stream()
                .filter(ac -> {
                    if (ac.getFechaRecogida() == null) return false;
                    LocalDate fecha = ac.getFechaRecogida().toLocalDate();
                    return fecha.getMonthValue() == mesActual && fecha.getYear() == anioActual;
                })
                .mapToDouble(ac -> ac.getProduccionReal() != null ? ac.getProduccionReal() : 0.0)
                .sum();

        return ResponseEntity.ok(DashboardResponse.builder()
                .totalAreas(totalAreas)
                .cultivosActivos(totalCultivosActivos)
                .riegosProximosSemana(totalRiegosProximosSemana)
                .produccionMesActual(produccionMes)
                .build());
    }
}
