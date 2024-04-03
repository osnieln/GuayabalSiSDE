package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.services.ProduccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "produccion")
public class ProduccionController {

    @Autowired
    ProduccionService produccionService;

    @GetMapping
    public ResponseEntity<List<Produccion>> findAll(){ return ResponseEntity.ok(produccionService.findAll()); }

    @GetMapping(path = "findById/{id}")
    public ResponseEntity<Produccion> findById(@PathVariable(required = true) Long id){
        return ResponseEntity.ok(produccionService.findById(id));
    }

    @PostMapping(path = "create")
    public ResponseEntity<Produccion> create(@RequestBody(required = true) Produccion produccion) {
        return ResponseEntity.ok(produccionService.create(produccion));
    }

    @PutMapping (path = "edit")
    public ResponseEntity<Produccion> edit(@RequestBody(required = true) Produccion produccion){
        return ResponseEntity.ok(produccionService.edit(produccion));
    }

    @DeleteMapping (path = "delete/{id}")
    public ResponseEntity<Produccion> delete(@PathVariable(required = true) Long id){
        return ResponseEntity.ok(produccionService.delete(id));
    }



}
