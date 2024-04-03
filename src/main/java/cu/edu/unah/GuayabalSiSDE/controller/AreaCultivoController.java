package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.services.AreaCultivoService;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "areaCultivo")
public class AreaCultivoController {

    @Autowired
    AreaCultivoService areaCultivoService;

    @GetMapping
    ResponseEntity<List<AreaCultivoResponse>> findAll(){
        List<AreaCultivo> areaCultivo = areaCultivoService.findAll();
        List<AreaCultivoResponse> areaCultivoResponseList = new ArrayList<>();
        for (AreaCultivo cultivo : areaCultivo) {
            areaCultivoResponseList.add(AreaCultivoResponse.AreaCultivoToAreaCultivoResponse(cultivo));
        }
        return ResponseEntity.ok(areaCultivoResponseList);
    }

    @PostMapping(path = "/findById")
    ResponseEntity<AreaCultivoResponse> findById(@RequestBody AreaCultivoPk areaCultivoPk){
        AreaCultivo areaCultivo = areaCultivoService.findById(areaCultivoPk);
        if(areaCultivo == null)
            return ResponseEntity.ok(null);
        return ResponseEntity.ok(AreaCultivoResponse.AreaCultivoToAreaCultivoResponse(areaCultivo));
    }

    @PostMapping(path = "/create")
    ResponseEntity<AreaCultivoResponse> create(@RequestBody AreaCultivoResponse areaCultivoResponse){
        AreaCultivo areaCultivo = areaCultivoService.create(AreaCultivoResponse.AreaCultivoResponseAreaCultivo(areaCultivoResponse));
        if (areaCultivo == null)
            return ResponseEntity.ok(null);
        return ResponseEntity.ok(AreaCultivoResponse
                .AreaCultivoToAreaCultivoResponse(areaCultivo));
    }

    @PutMapping(path = "/edit")
    ResponseEntity<AreaCultivoResponse> edit(@RequestBody AreaCultivoResponse areaCultivoResponse){
        AreaCultivo areaCultivo = areaCultivoService.edit(AreaCultivoResponse.AreaCultivoResponseAreaCultivo(areaCultivoResponse));
        if (areaCultivo == null)
            return ResponseEntity.ok(null);
        return ResponseEntity.ok(AreaCultivoResponse
                .AreaCultivoToAreaCultivoResponse(areaCultivo));
    }

    @DeleteMapping(path = "/delete")
    ResponseEntity<AreaCultivoResponse> delete(@RequestBody AreaCultivoPk areaCultivoPk){
        AreaCultivo areaCultivo = areaCultivoService.delete(areaCultivoPk);
        if (areaCultivo == null)
            return ResponseEntity.ok(null);
        return ResponseEntity.ok(AreaCultivoResponse
                .AreaCultivoToAreaCultivoResponse(areaCultivo));
    }
}
