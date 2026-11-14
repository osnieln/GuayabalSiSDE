package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.services.AlertaSistemaService;
import cu.edu.unah.GuayabalSiSDE.util.AlertaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "alertas")
public class AlertaController {

    @Autowired
    private AlertaSistemaService alertaSistemaService;

    @GetMapping
    public ResponseEntity<List<AlertaResponse>> findAll(
            @RequestParam(required = false, defaultValue = "false") boolean soloNoLeidas) {
        List<AlertaResponse> alertas = alertaSistemaService.findAll(soloNoLeidas).stream()
                .map(AlertaResponse::map)
                .collect(Collectors.toList());
        return ResponseEntity.ok(alertas);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(alertaSistemaService.countActivas());
    }

    @PutMapping("/{id}/leida")
    public ResponseEntity<AlertaResponse> marcarLeida(@PathVariable Long id) {
        return ResponseEntity.ok(AlertaResponse.map(alertaSistemaService.marcarLeida(id)));
    }

    @PutMapping("/{id}/resuelta")
    public ResponseEntity<AlertaResponse> marcarResuelta(@PathVariable Long id) {
        return ResponseEntity.ok(AlertaResponse.map(alertaSistemaService.marcarResuelta(id)));
    }

    @PostMapping("/generar")
    public ResponseEntity<Void> generar() {
        alertaSistemaService.generarAlertas();
        return ResponseEntity.ok().build();
    }
}
