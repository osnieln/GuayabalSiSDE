package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.ConfiguracionCorreo;
import cu.edu.unah.GuayabalSiSDE.services.ConfiguracionCorreoService;
import cu.edu.unah.GuayabalSiSDE.util.ConfiguracionCorreoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config/correo")
public class ConfiguracionCorreoController {

    @Autowired
    private ConfiguracionCorreoService service;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getConfig() {
        ConfiguracionCorreo cfg = service.getConfig();
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("host", cfg.getHost() != null ? cfg.getHost() : "smtp.gmail.com");
        r.put("puerto", cfg.getPuerto() > 0 ? cfg.getPuerto() : 587);
        r.put("usuario", cfg.getUsuario() != null ? cfg.getUsuario() : "");
        r.put("contrasena", (cfg.getContrasena() != null && !cfg.getContrasena().isBlank()) ? "*****" : "");
        r.put("remitente", cfg.getRemitente() != null ? cfg.getRemitente() : "");
        r.put("activo", cfg.isActivo());
        return ResponseEntity.ok(r);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> saveConfig(@RequestBody ConfiguracionCorreoDTO dto) {
        try {
            service.saveConfig(dto);
            Map<String, String> r = new LinkedHashMap<>();
            r.put("status", "ok");
            r.put("mensaje", "Configuración guardada correctamente.");
            return ResponseEntity.ok(r);
        } catch (Exception e) {
            Map<String, String> r = new LinkedHashMap<>();
            r.put("status", "error");
            r.put("mensaje", e.getMessage() != null ? e.getMessage() : "Error al guardar la configuración.");
            return ResponseEntity.internalServerError().body(r);
        }
    }

    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> testConexion(@RequestBody ConfiguracionCorreoDTO dto) {
        Map<String, String> r = new LinkedHashMap<>();
        try {
            service.testConexion(dto);
            r.put("status", "ok");
            r.put("mensaje", "Conexión exitosa al servidor SMTP.");
        } catch (Exception e) {
            r.put("status", "error");
            String msg = e.getMessage();
            r.put("mensaje", msg != null && !msg.isBlank() ? msg : "No se pudo conectar al servidor SMTP.");
        }
        return ResponseEntity.ok(r);
    }
}
