package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.entity.Riego;
import cu.edu.unah.GuayabalSiSDE.services.AreaCultivoService;
import cu.edu.unah.GuayabalSiSDE.services.RiegoService;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponsePK;
import cu.edu.unah.GuayabalSiSDE.util.DateFormatter;
import cu.edu.unah.GuayabalSiSDE.util.RiegoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "riego")
public class RiegoController {

    @Autowired
    RiegoService riegoService;

    @Autowired
    AreaCultivoService areaCultivoService;

    @GetMapping
    public ResponseEntity<List<RiegoResponse>> findAll() {
        List<Riego> riegos = riegoService.findAll();
        List<RiegoResponse> riegoResponses = new ArrayList<>();
        for (Riego riego : riegos) {
            riegoResponses.add(RiegoResponse.map(riego));
        }
        return ResponseEntity.ok(riegoResponses);
    }

    @GetMapping(path = "/findById/{id}")
    public ResponseEntity<RiegoResponse> findById(@PathVariable(required = true) Long id) {
        Riego riego = riegoService.findById(id);
        if (riego == null) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(RiegoResponse.map(riego));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<RiegoResponse> create(@RequestBody RiegoResponse riegoResponse) {
        AreaCultivo areaCultivoDb = areaCultivoService.findById(AreaCultivoResponsePK.map(riegoResponse.getAreaCultivoResponsePk()));
        if (areaCultivoDb == null) {
            return ResponseEntity.ok(null);
        }
        Riego riego = RiegoResponse.map(riegoResponse, areaCultivoDb);
        return ResponseEntity.ok(RiegoResponse.map(
                        riegoService.create(riego)
                )
        );
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<RiegoResponse> edit(@RequestBody RiegoResponse riegoResponse) {
        AreaCultivo areaCultivoDb = areaCultivoService.findById(AreaCultivoResponsePK.map(riegoResponse.getAreaCultivoResponsePk()));
        if (areaCultivoDb == null) {
            return ResponseEntity.ok(null);
        }
        Riego riego = RiegoResponse.map(riegoResponse, areaCultivoDb);
        return ResponseEntity.ok(RiegoResponse.map(
                        riegoService.edit(riego)
                )
        );
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Riego> create(@PathVariable Long id) {
        return ResponseEntity.ok(riegoService.delete(id));
    }
}
