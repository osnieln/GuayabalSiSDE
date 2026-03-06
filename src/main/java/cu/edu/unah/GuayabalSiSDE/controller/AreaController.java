package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.Area;
import cu.edu.unah.GuayabalSiSDE.services.AreaService;
import cu.edu.unah.GuayabalSiSDE.util.AreaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "area")
public class AreaController {

    @Autowired
    AreaService areaService;

    @GetMapping
    public ResponseEntity<List<AreaResponse>> findAll(){
        List<Area> areaList = areaService.findAll();
        List<AreaResponse> areaResponseList = new ArrayList<>();
        for (Area area : areaList) {
            areaResponseList.add(AreaResponse.AreaToAreaResponse(area));
        }
        return ResponseEntity.ok(areaResponseList);
    }

    @GetMapping(path = "/findById/{id}")
    public ResponseEntity<AreaResponse> findById(@PathVariable(required = true) Long id){
        Area area = areaService.findByID(id);
        if(area == null)
            return ResponseEntity.ok(null);
        return ResponseEntity.ok(AreaResponse.AreaToAreaResponse(area));
    }

    @PostMapping(path = "/create")
    public ResponseEntity<AreaResponse> create(@RequestBody AreaResponse areaResponse){
        return ResponseEntity.ok(
                AreaResponse.AreaToAreaResponse(
                    areaService.createArea(
                            AreaResponse.AreaResponseToArea(areaResponse)
                    )
            )
        );
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<AreaResponse> edit(@RequestBody AreaResponse areaResponse){
        return ResponseEntity.ok(
                AreaResponse.AreaToAreaResponse(
                        areaService.editArea(
                                AreaResponse.AreaResponseToArea(areaResponse)
                        )
                )
        );
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<AreaResponse> create(@PathVariable Long id){
        return ResponseEntity.ok(
                AreaResponse.AreaToAreaResponse(areaService.deleteArea(id))
        );
    }

    @GetMapping(path = "/findDistinctCapa")
    public List<String> findDistinctCapa(){
        return areaService.findDistinctCapa();
    }

    @GetMapping(path = "/findByCapa/{capa}")
    public ResponseEntity<List<AreaResponse>> findByCapa(@PathVariable String capa){
        List<Area> areas = areaService.findByCapa(capa);
        List<AreaResponse> responses = new ArrayList<>();
        for (Area area : areas) {
            responses.add(AreaResponse.AreaToAreaResponse(area));
        }
        return ResponseEntity.ok(responses);
    }

    @GetMapping(path = "/exportar/csv")
    public ResponseEntity<byte[]> exportarCsv(){
        List<Area> areas = areaService.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("id,descripcion,capa\n");
        for (Area a : areas) {
            sb.append(a.getId()).append(",")
              .append(a.getDescripcion() != null ? a.getDescripcion() : "").append(",")
              .append(a.getCapa() != null ? a.getCapa() : "").append("\n");
        }
        byte[] csvBytes = sb.toString().getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "areas.csv");
        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }
}
