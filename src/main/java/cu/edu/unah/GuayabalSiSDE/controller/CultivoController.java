package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.services.CultivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping (path = "create")
    public ResponseEntity<Cultivo> create(@RequestBody(required = true) Cultivo cultivo){
        return ResponseEntity.ok(cultivoService.create(cultivo));
    }

    @PutMapping (path = "edit")
    public ResponseEntity<Cultivo> edit(@RequestBody(required = true) Cultivo cultivo){
        return ResponseEntity.ok(cultivoService.edit(cultivo));
    }

    @DeleteMapping (path = "delete/{id}")
    public ResponseEntity<Cultivo> delete(@PathVariable(required = true) Long id){
        return ResponseEntity.ok(cultivoService.delete(id));
    }

    @GetMapping(path = "buscar")
    public ResponseEntity<List<Cultivo>> buscar(@RequestParam String texto){
        return ResponseEntity.ok(cultivoService.buscarPorTexto(texto));
    }

    @GetMapping(path = "exportar/csv")
    public ResponseEntity<byte[]> exportarCsv(){
        List<Cultivo> cultivos = cultivoService.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("id,descripcion,produccion,tipoCultivo\n");
        for (Cultivo c : cultivos) {
            sb.append(c.getId()).append(",")
              .append(c.getDescripcion() != null ? c.getDescripcion() : "").append(",")
              .append(c.getProduccion() != null ? c.getProduccion().getDescripcion() : "").append(",")
              .append(c.getTipoCultivo() != null ? c.getTipoCultivo().getNombre() : "").append("\n");
        }
        byte[] csvBytes = sb.toString().getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "cultivos.csv");
        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }
}
