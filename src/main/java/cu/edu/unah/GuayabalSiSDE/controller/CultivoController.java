package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.services.CultivoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping(path = "cultivo")
public class CultivoController {

    @Autowired
    CultivoService cultivoService;

    @GetMapping
    public ResponseEntity<List<Cultivo>> findAll(){
        return ResponseEntity.ok(cultivoService.findAll());
    }

    @GetMapping(path = "findById/{id}")
    public ResponseEntity<Cultivo> findById(@PathVariable(required = true) Long id){
        return ResponseEntity.ok(cultivoService.findById(id));
    }

    @GetMapping(path = "findByProduccion/{id}")
    public ResponseEntity<List<Cultivo>> findByProduccion(@PathVariable(required = true) Long id){
        return ResponseEntity.ok(cultivoService.findByProduccion(id));
    }

    @GetMapping(path = "search/{texto}")
    public ResponseEntity<List<Cultivo>> searchByDescripcion(@PathVariable String texto){
        return ResponseEntity.ok(cultivoService.findByDescripcion(texto));
    }

    @PostMapping (path = "create")
    public ResponseEntity<Cultivo> create(@Valid @RequestBody(required = true) Cultivo cultivo){
        return ResponseEntity.ok(cultivoService.create(cultivo));
    }

    @PutMapping (path = "edit")
    public ResponseEntity<Cultivo> edit(@Valid @RequestBody(required = true) Cultivo cultivo){
        return ResponseEntity.ok(cultivoService.edit(cultivo));
    }

    @DeleteMapping (path = "delete/{id}")
    public ResponseEntity<Cultivo> delete(@PathVariable(required = true) Long id){
        return ResponseEntity.ok(cultivoService.delete(id));
    }

    @GetMapping(path = "export/csv")
    public ResponseEntity<byte[]> exportCsv(){
        List<Cultivo> cultivos = cultivoService.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Descripcion,Tipo Cultivo,Produccion\n");
        for (Cultivo c : cultivos) {
            sb.append(c.getId()).append(",")
              .append(csvEscape(c.getDescripcion())).append(",")
              .append(c.getTipoCultivo() != null ? csvEscape(c.getTipoCultivo().getNombre()) : "").append(",")
              .append(c.getProduccion() != null ? csvEscape(c.getProduccion().getDescripcion()) : "").append("\n");
        }
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "cultivos.csv");
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    private String csvEscape(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
