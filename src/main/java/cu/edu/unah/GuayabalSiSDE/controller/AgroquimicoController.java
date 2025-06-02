package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.Agroquimico;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.services.AgroquimicoService;
import cu.edu.unah.GuayabalSiSDE.services.AreaCultivoService;
import cu.edu.unah.GuayabalSiSDE.util.AgroquimicoResponse;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "agroquimico")
public class AgroquimicoController {
    
    @Autowired
    private AgroquimicoService agroquimicoService;

    @Autowired
    @Lazy
    private AreaCultivoService areaCultivoService;

    @GetMapping
    public ResponseEntity<List<AgroquimicoResponse>> findAll() {
        List<Agroquimico> list = agroquimicoService.findAll();
        List<AgroquimicoResponse> listResponse = new ArrayList<>();
        list.forEach(agroquimico -> listResponse.add(AgroquimicoResponse.map(agroquimico)));
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping(path = "/findById/{id}")
    public ResponseEntity<AgroquimicoResponse> findById(@PathVariable(required = true) Long id) {
        return ResponseEntity.ok(AgroquimicoResponse.map(agroquimicoService.findById(id)));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<AgroquimicoResponse> create(@RequestBody AgroquimicoResponse agroquimicoResponse) {
        Agroquimico agroquimico = AgroquimicoResponse.map(agroquimicoResponse);
        List<AreaCultivo> areaCultivos = new ArrayList<>();
        agroquimico.getAreaCultivos().forEach(a->{
            AreaCultivo areaCultivo = areaCultivoService.findById(a.getAreaCultivoPk());
            if(areaCultivo == null) {
                throw new BusinessValidationException(
                        ErrorCodes.OPERATION_VALIDATION_ERROR,
                        "El area cultivo con id " + a.getAreaCultivoPk() + " no existe."
                );
            }
            areaCultivos.add(areaCultivo);
        });
        agroquimico.setAreaCultivos(areaCultivos);
        return ResponseEntity.ok(AgroquimicoResponse.map(agroquimicoService.create(agroquimico)));
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<AgroquimicoResponse> edit(@RequestBody AgroquimicoResponse agroquimicoResponse) {
        Agroquimico agroquimico = AgroquimicoResponse.map(agroquimicoResponse);
        List<AreaCultivo> areaCultivos = new ArrayList<>();
        agroquimico.getAreaCultivos().forEach(a->{
            AreaCultivo areaCultivo = areaCultivoService.findById(a.getAreaCultivoPk());
            if(areaCultivo == null) {
                throw new BusinessValidationException(
                        ErrorCodes.OPERATION_VALIDATION_ERROR,
                        "El area cultivo con id " + a.getAreaCultivoPk() + " no existe."
                );
            }
            areaCultivos.add(areaCultivo);
        });
        agroquimico.setAreaCultivos(areaCultivos);
        return ResponseEntity.ok(AgroquimicoResponse.map(agroquimicoService.edit(agroquimico)));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<AgroquimicoResponse> create(@PathVariable Long id) {
        return ResponseEntity.ok(AgroquimicoResponse.map(agroquimicoService.delete(id)));
    }
}
