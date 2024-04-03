package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.services.CultivoService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
