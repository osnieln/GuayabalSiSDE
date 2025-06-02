package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;
import cu.edu.unah.GuayabalSiSDE.services.TipoCultivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "tipoCultivo")
public class TipoCultivoController {

    @Autowired
    TipoCultivoService tipoCultivoService;

    @GetMapping
    public ResponseEntity<List<TipoCultivo>> findAll() {
        return ResponseEntity.ok(tipoCultivoService.findAll());
    }

    @GetMapping(path = "/findById/{id}")
    public ResponseEntity<TipoCultivo> findById(@PathVariable(required = true) Long id) {
        return ResponseEntity.ok(tipoCultivoService.findById(id));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<TipoCultivo> create(@RequestBody TipoCultivo tipoCultivo) {
        return ResponseEntity.ok(
                tipoCultivoService.create(tipoCultivo)
        );
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<TipoCultivo> edit(@RequestBody TipoCultivo tipoCultivo) {
        return ResponseEntity.ok(tipoCultivoService.edit(tipoCultivo));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<TipoCultivo> create(@PathVariable Long id) {
        return ResponseEntity.ok(tipoCultivoService.delete(id));
    }
}
