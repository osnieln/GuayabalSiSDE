package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.*;
import cu.edu.unah.GuayabalSiSDE.services.AgroquimicoService;
import cu.edu.unah.GuayabalSiSDE.services.AreaCultivoService;
import cu.edu.unah.GuayabalSiSDE.services.AreaService;
import cu.edu.unah.GuayabalSiSDE.services.CultivoService;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponse;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponsePK;
import cu.edu.unah.GuayabalSiSDE.util.DateFormatter;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "areaCultivo")
public class AreaCultivoController {

    @Autowired
    AreaCultivoService areaCultivoService;

    @Autowired
    @Lazy
    AreaService areaService;

    @Autowired
    @Lazy
    CultivoService cultivoService;

    @Autowired
    @Lazy
    AgroquimicoService agroquimicoService;

    @GetMapping
    ResponseEntity<List<AreaCultivoResponse>> findAll(){
        List<AreaCultivo> areaCultivo = areaCultivoService.findAll();
        List<AreaCultivoResponse> areaCultivoResponseList = new ArrayList<>();
        for (AreaCultivo cultivo : areaCultivo) {
            areaCultivoResponseList.add(AreaCultivoResponse.map(cultivo));
        }
        return ResponseEntity.ok(areaCultivoResponseList);
    }

    @PostMapping(path = "/findById")
    ResponseEntity<AreaCultivoResponse> findById(@RequestBody AreaCultivoResponsePK areaCultivoResponsePK){
        AreaCultivoPk areaCultivoPk = AreaCultivoPk.builder()
                .areaId(areaCultivoResponsePK.getAreaId())
                .cultivoId(areaCultivoResponsePK.getCultivoId())
                .fechaSiembra(DateFormatter.format(areaCultivoResponsePK.getFechaSiembra()))
                .build();
        AreaCultivo areaCultivo = areaCultivoService.findById(areaCultivoPk);
        if(areaCultivo == null)
            return ResponseEntity.ok(null);
        return ResponseEntity.ok(AreaCultivoResponse.map(areaCultivo));
    }

    @PostMapping(path = "/create")
    ResponseEntity<AreaCultivoResponse> create(@RequestBody AreaCultivoResponse areaCultivoResponse){
        Area area = areaService.findByID(areaCultivoResponse.getAreaCultivoResponsePK().getAreaId());
        Cultivo cultivo = cultivoService.findById(areaCultivoResponse.getAreaCultivoResponsePK().getCultivoId());
        List<Agroquimico> agroquimicoList = new ArrayList<>();
        areaCultivoResponse.getAgroquimicos().forEach(agroquimico -> {
            Agroquimico agroquimicoDb = agroquimicoService.findByNombre(agroquimico);
            if(null != agroquimicoDb)
                agroquimicoList.add(agroquimicoDb);
        });
        AreaCultivo areaCultivo = areaCultivoService.create(AreaCultivoResponse.map(areaCultivoResponse, area, cultivo, agroquimicoList));
        if (areaCultivo == null)
            return ResponseEntity.ok(null);
        return ResponseEntity.ok(AreaCultivoResponse
                .map(areaCultivo));
    }

    @PutMapping(path = "/edit")
    ResponseEntity<AreaCultivoResponse> edit(@RequestBody AreaCultivoResponse areaCultivoResponse){
        Area area = areaService.findByID(areaCultivoResponse.getAreaCultivoResponsePK().getAreaId());
        Cultivo cultivo = cultivoService.findById(areaCultivoResponse.getAreaCultivoResponsePK().getCultivoId());
        List<Agroquimico> agroquimicoList = new ArrayList<>();
        areaCultivoResponse.getAgroquimicos().forEach(agroquimico -> {
            Agroquimico agroquimicoDb = agroquimicoService.findByNombre(agroquimico);
            if(null != agroquimicoDb)
                agroquimicoList.add(agroquimicoDb);
        });
        AreaCultivo areaCultivo = areaCultivoService.edit(AreaCultivoResponse.map(areaCultivoResponse, area, cultivo, agroquimicoList));
        if (areaCultivo == null)
            return ResponseEntity.ok(null);
        return ResponseEntity.ok(AreaCultivoResponse
                .map(areaCultivo));
    }

    @PostMapping(path = "/delete")
    ResponseEntity<AreaCultivoResponse> delete(@RequestBody AreaCultivoResponsePK areaCultivoResponsePK){
        AreaCultivoPk areaCultivoPk = AreaCultivoPk.builder()
                .areaId(areaCultivoResponsePK.getAreaId())
                .cultivoId(areaCultivoResponsePK.getCultivoId())
                .fechaSiembra(DateFormatter.format(areaCultivoResponsePK.getFechaSiembra()))
                .build();
        AreaCultivo areaCultivo = areaCultivoService.delete(areaCultivoPk);
        if (areaCultivo == null)
            return ResponseEntity.ok(null);
        return ResponseEntity.ok(AreaCultivoResponse
                .map(areaCultivo));
    }

    @GetMapping(path = "/findByPlanProdBetween/{planProd}/{planProd2}")
    ResponseEntity<List<AreaCultivoResponse>> findAreaCultivoByPlanProdBetween(@PathVariable Long planProd, @PathVariable Long planProd2){
        List<AreaCultivo> areaCultivoList = areaCultivoService.findAreaCultivoByPlanProdBetween(planProd, planProd2);
        if(areaCultivoList.isEmpty())
            return ResponseEntity.ok(null);
        List<AreaCultivoResponse> areaCultivoResponseList = new ArrayList<>();
        areaCultivoList.forEach(areaCultivo -> {
            areaCultivoResponseList.add(AreaCultivoResponse.map(areaCultivo));
        });
        return ResponseEntity.ok(areaCultivoResponseList);
    }

    @GetMapping(path = "/findByProdCultivosPermanenteAfter/{prodCultivosPermanente}")
    ResponseEntity<List<AreaCultivoResponse>> findAreaCultivoByProdCultivosPermanenteAfter(@PathVariable Double prodCultivosPermanente){
        List<AreaCultivo> areaCultivoList = areaCultivoService.findAreaCultivoByProdCultivosPermanenteAfter(prodCultivosPermanente);
        if(areaCultivoList.isEmpty())
            return ResponseEntity.ok(null);
        List<AreaCultivoResponse> areaCultivoResponseList = new ArrayList<>();
        areaCultivoList.forEach(areaCultivo -> {
            areaCultivoResponseList.add(AreaCultivoResponse.map(areaCultivo));
        });
        return ResponseEntity.ok(areaCultivoResponseList);
    }

    @GetMapping(path = "/findByFechaRecogidaBefore/{fechaRecogida}")
    ResponseEntity<List<AreaCultivoResponse>> findAreaCultivoByFechaRecogidaBefore(@PathVariable String fechaRecogida){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = new Date(formatter.parse(fechaRecogida).getTime());
        } catch (ParseException e) {
            throw new BusinessValidationException(ErrorCodes.INVALID_DATE_FORMAT, "El formato de fecha introducido no es válido. La fecha debe tener este formato dd-MMM-yyyy.");
        }
        List<AreaCultivo> areaCultivoList = areaCultivoService.findAreaCultivoByFechaRecogidaBefore(date);
        if(areaCultivoList.isEmpty())
            return ResponseEntity.ok(null);
        List<AreaCultivoResponse> areaCultivoResponseList = new ArrayList<>();
        areaCultivoList.forEach(areaCultivo -> {
            areaCultivoResponseList.add(AreaCultivoResponse.map(areaCultivo));
        });
        return ResponseEntity.ok(areaCultivoResponseList);
    }
}
