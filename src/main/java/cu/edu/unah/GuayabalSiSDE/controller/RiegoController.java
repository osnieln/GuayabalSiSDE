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
import java.util.concurrent.TimeUnit;

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
        Riego saved = riegoService.create(riego);
        RiegoResponse response = RiegoResponse.map(saved);
        response.setAdvertencia(calcularAdvertenciaFecha(saved));
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/findByAreaCultivoPk")
    public ResponseEntity<List<RiegoResponse>> findByAreaCultivo(@RequestBody AreaCultivoResponsePK areaCultivoResponsePK) {
        AreaCultivoPk areaCultivoPk = AreaCultivoPk.builder()
                .areaId(areaCultivoResponsePK.getAreaId())
                .cultivoId(areaCultivoResponsePK.getCultivoId())
                .fechaSiembra(DateFormatter.format(areaCultivoResponsePK.getFechaSiembra()))
                .build();
        List<Riego> riegoList = riegoService.findByAreaCultivoPk(areaCultivoPk);
        return ResponseEntity.ok(riegoList.stream().map(RiegoResponse :: map).toList());
    }

    @PutMapping(path = "/edit")
    public ResponseEntity<RiegoResponse> edit(@RequestBody RiegoResponse riegoResponse) {
        AreaCultivo areaCultivoDb = areaCultivoService.findById(AreaCultivoResponsePK.map(riegoResponse.getAreaCultivoResponsePk()));
        if (areaCultivoDb == null) {
            return ResponseEntity.ok(null);
        }
        Riego riego = RiegoResponse.map(riegoResponse, areaCultivoDb);
        Riego edited = riegoService.edit(riego);
        RiegoResponse response = RiegoResponse.map(edited);
        response.setAdvertencia(calcularAdvertenciaFecha(edited));
        return ResponseEntity.ok(response);
    }

    private String calcularAdvertenciaFecha(Riego riego) {
        if (riego.getFechaReal() == null || riego.getFechaPlanificacion() == null) return null;
        long diffMs = Math.abs(riego.getFechaReal().getTime() - riego.getFechaPlanificacion().getTime());
        long diffDias = TimeUnit.MILLISECONDS.toDays(diffMs);
        if (diffDias > 7) {
            return "Advertencia: la fecha real de riego difiere " + diffDias + " días de la fecha planificada.";
        }
        return null;
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Riego> create(@PathVariable Long id) {
        return ResponseEntity.ok(riegoService.delete(id));
    }

    @GetMapping(path = "/proximos/{dias}")
    public ResponseEntity<List<RiegoResponse>> findRiegosProximos(@PathVariable int dias) {
        List<Riego> riegos = riegoService.findRiegosProximos(dias);
        return ResponseEntity.ok(riegos.stream().map(RiegoResponse::map).toList());
    }

    @GetMapping(path = "/historial/area/{areaId}")
    public ResponseEntity<List<RiegoResponse>> findHistorialByArea(@PathVariable Long areaId) {
        List<Riego> riegos = riegoService.findHistorialByArea(areaId);
        return ResponseEntity.ok(riegos.stream().map(RiegoResponse::map).toList());
    }
}
