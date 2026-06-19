package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.Agroquimico;
import cu.edu.unah.GuayabalSiSDE.entity.MovimientoAgroquimico;
import cu.edu.unah.GuayabalSiSDE.entity.TipoMovimiento;
import cu.edu.unah.GuayabalSiSDE.entity.Trabajador;
import cu.edu.unah.GuayabalSiSDE.services.AgroquimicoService;
import cu.edu.unah.GuayabalSiSDE.services.MovimientoAgroquimicoService;
import cu.edu.unah.GuayabalSiSDE.services.TrabajadorService;
import cu.edu.unah.GuayabalSiSDE.util.DateFormatter;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import cu.edu.unah.GuayabalSiSDE.util.MovimientoAgroquimicoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "movimientoAgroquimico")
public class MovimientoAgroquimicoController {

    @Autowired
    private MovimientoAgroquimicoService movimientoAgroquimicoService;

    @Autowired
    private AgroquimicoService agroquimicoService;

    @Autowired
    private TrabajadorService trabajadorService;

    @GetMapping
    public ResponseEntity<List<MovimientoAgroquimicoResponse>> findAll() {
        List<MovimientoAgroquimico> list = movimientoAgroquimicoService.findAll();
        List<MovimientoAgroquimicoResponse> listResponse = new ArrayList<>();
        list.forEach(movimiento -> listResponse.add(MovimientoAgroquimicoResponse.map(movimiento)));
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping(path = "/findById/{id}")
    public ResponseEntity<MovimientoAgroquimicoResponse> findById(@PathVariable(required = true) Long id) {
        return ResponseEntity.ok(MovimientoAgroquimicoResponse.map(movimientoAgroquimicoService.findById(id)));
    }

    @GetMapping(path = "/agroquimico/{agroquimicoId}")
    public ResponseEntity<List<MovimientoAgroquimicoResponse>> findByAgroquimico(@PathVariable Long agroquimicoId) {
        List<MovimientoAgroquimico> list = movimientoAgroquimicoService.findByAgroquimico(agroquimicoId);
        List<MovimientoAgroquimicoResponse> listResponse = new ArrayList<>();
        list.forEach(movimiento -> listResponse.add(MovimientoAgroquimicoResponse.map(movimiento)));
        return ResponseEntity.ok(listResponse);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<MovimientoAgroquimicoResponse> create(@RequestBody MovimientoAgroquimicoResponse movimientoAgroquimicoResponse) {
        MovimientoAgroquimico movimiento = mapToEntity(movimientoAgroquimicoResponse);
        return ResponseEntity.ok(MovimientoAgroquimicoResponse.map(movimientoAgroquimicoService.create(movimiento)));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<MovimientoAgroquimicoResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(MovimientoAgroquimicoResponse.map(movimientoAgroquimicoService.delete(id)));
    }

    private MovimientoAgroquimico mapToEntity(MovimientoAgroquimicoResponse response) {
        if (response.getAgroquimicoId() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El agroquímico del movimiento es obligatorio.");
        Agroquimico agroquimico = agroquimicoService.findById(response.getAgroquimicoId());
        if (agroquimico == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "El agroquímico con id " + response.getAgroquimicoId() + " no existe.");

        Trabajador trabajador = null;
        if (response.getTrabajadorId() != null) {
            trabajador = trabajadorService.findById(response.getTrabajadorId());
            if (trabajador == null)
                throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "El trabajador con id " + response.getTrabajadorId() + " no existe.");
        }

        return MovimientoAgroquimico.builder()
                .id(response.getId())
                .agroquimico(agroquimico)
                .tipo(response.getTipo() != null ? TipoMovimiento.valueOf(response.getTipo()) : null)
                .cantidad(response.getCantidad())
                .fecha(response.getFecha() != null ? DateFormatter.format(response.getFecha()) : null)
                .trabajador(trabajador)
                .observaciones(response.getObservaciones())
                .build();
    }
}
