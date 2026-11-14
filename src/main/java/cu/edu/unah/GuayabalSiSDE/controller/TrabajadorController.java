package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.Area;
import cu.edu.unah.GuayabalSiSDE.entity.Riego;
import cu.edu.unah.GuayabalSiSDE.entity.Trabajador;
import cu.edu.unah.GuayabalSiSDE.services.AreaService;
import cu.edu.unah.GuayabalSiSDE.services.RiegoService;
import cu.edu.unah.GuayabalSiSDE.services.TrabajadorService;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import cu.edu.unah.GuayabalSiSDE.util.TrabajadorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "trabajador")
public class TrabajadorController {

    @Autowired
    private TrabajadorService trabajadorService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private RiegoService riegoService;

    @GetMapping
    public ResponseEntity<List<TrabajadorResponse>> findAll() {
        List<Trabajador> list = trabajadorService.findAll();
        List<TrabajadorResponse> listResponse = new ArrayList<>();
        list.forEach(trabajador -> listResponse.add(TrabajadorResponse.map(trabajador)));
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping(path = "/findById/{id}")
    public ResponseEntity<TrabajadorResponse> findById(@PathVariable(required = true) Long id) {
        return ResponseEntity.ok(TrabajadorResponse.map(trabajadorService.findById(id)));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<TrabajadorResponse> create(@RequestBody TrabajadorResponse trabajadorResponse) {
        Trabajador trabajador = TrabajadorResponse.map(trabajadorResponse);
        trabajador.setAreas(resolveAreas(trabajador.getAreas()));
        trabajador.setRiegos(resolveRiegos(trabajador.getRiegos()));
        return ResponseEntity.ok(TrabajadorResponse.map(trabajadorService.create(trabajador)));
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<TrabajadorResponse> edit(@RequestBody TrabajadorResponse trabajadorResponse) {
        Trabajador trabajador = TrabajadorResponse.map(trabajadorResponse);
        trabajador.setAreas(resolveAreas(trabajador.getAreas()));
        trabajador.setRiegos(resolveRiegos(trabajador.getRiegos()));
        return ResponseEntity.ok(TrabajadorResponse.map(trabajadorService.edit(trabajador)));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<TrabajadorResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(TrabajadorResponse.map(trabajadorService.delete(id)));
    }

    private List<Area> resolveAreas(List<Area> areas) {
        List<Area> resolved = new ArrayList<>();
        areas.forEach(a -> {
            Area area = areaService.findByID(a.getId());
            if (area == null) {
                throw new BusinessValidationException(
                        ErrorCodes.OPERATION_VALIDATION_ERROR,
                        "El área con id " + a.getId() + " no existe."
                );
            }
            resolved.add(area);
        });
        return resolved;
    }

    private List<Riego> resolveRiegos(List<Riego> riegos) {
        List<Riego> resolved = new ArrayList<>();
        riegos.forEach(r -> {
            Riego riego = riegoService.findById(r.getId());
            if (riego == null) {
                throw new BusinessValidationException(
                        ErrorCodes.OPERATION_VALIDATION_ERROR,
                        "El riego con id " + r.getId() + " no existe."
                );
            }
            resolved.add(riego);
        });
        return resolved;
    }
}
