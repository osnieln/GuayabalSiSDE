package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.services.DashboardService;
import cu.edu.unah.GuayabalSiSDE.util.DashboardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "dashboard")
public class DashboardController {

    @Autowired
    DashboardService dashboardService;

    @GetMapping(path = "/estadisticas")
    public ResponseEntity<DashboardResponse> getEstadisticas() {
        return ResponseEntity.ok(dashboardService.getEstadisticas());
    }
}
