package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.*;
import cu.edu.unah.GuayabalSiSDE.reportes.ExcelExportService;
import cu.edu.unah.GuayabalSiSDE.services.AgroquimicoService;
import cu.edu.unah.GuayabalSiSDE.services.AreaCultivoService;
import cu.edu.unah.GuayabalSiSDE.services.AreaService;
import cu.edu.unah.GuayabalSiSDE.services.CultivoService;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponse;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponsePK;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponseReport;
import cu.edu.unah.GuayabalSiSDE.util.DateFormatter;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import cu.edu.unah.GuayabalSiSDE.util.RendimientoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
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

    @Autowired
    private ExcelExportService excelExportService;

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
    public ResponseEntity<List<AreaCultivoResponseReport>> findAreaCultivoByPlanProdBetween(@PathVariable Long planProd, @PathVariable Long planProd2){
        List<AreaCultivo> areaCultivoList = areaCultivoService.findAreaCultivoByPlanProdBetween(planProd, planProd2);
        if(areaCultivoList.isEmpty())
            return ResponseEntity.ok(new ArrayList<>());
        List<AreaCultivoResponseReport> areaCultivoResponseList = new ArrayList<>();
        areaCultivoList.forEach(areaCultivo -> {
            areaCultivoResponseList.add(AreaCultivoResponseReport.map(areaCultivo));
        });
        return ResponseEntity.ok(areaCultivoResponseList);
    }

    @GetMapping(path = "/findByProdCultivosPermanenteAfter/{prodCultivosPermanente}")
    public ResponseEntity<List<AreaCultivoResponseReport>> findAreaCultivoByProdCultivosPermanenteAfter(@PathVariable Double prodCultivosPermanente){
        List<AreaCultivo> areaCultivoList = areaCultivoService.findAreaCultivoByProdCultivosPermanenteAfter(prodCultivosPermanente);
        if(areaCultivoList.isEmpty())
            return ResponseEntity.ok(new ArrayList<>());
        List<AreaCultivoResponseReport> areaCultivoResponseList = new ArrayList<>();
        areaCultivoList.forEach(areaCultivo -> {
            areaCultivoResponseList.add(AreaCultivoResponseReport.map(areaCultivo));
        });
        return ResponseEntity.ok(areaCultivoResponseList);
    }

    @GetMapping(path = "/findByFechaRecogidaBefore/{fechaRecogida}")
    public ResponseEntity<List<AreaCultivoResponseReport>> findAreaCultivoByFechaRecogidaBefore(@PathVariable String fechaRecogida){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = new Date(formatter.parse(fechaRecogida).getTime());
        } catch (ParseException e) {
            throw new BusinessValidationException(ErrorCodes.INVALID_DATE_FORMAT, "El formato de fecha introducido no es válido. La fecha debe tener este formato dd-MM-yyyy.");
        }
        List<AreaCultivo> areaCultivoList = areaCultivoService.findAreaCultivoByFechaRecogidaBefore(date);
        if(areaCultivoList.isEmpty())
            return ResponseEntity.ok(new ArrayList<>());
        List<AreaCultivoResponseReport> areaCultivoResponseList = new ArrayList<>();
        areaCultivoList.forEach(areaCultivo -> {
            areaCultivoResponseList.add(AreaCultivoResponseReport.map(areaCultivo));
        });
        return ResponseEntity.ok(areaCultivoResponseList);
    }

    @GetMapping(path = "/calendario/{desde}/{hasta}")
    public ResponseEntity<List<AreaCultivoResponse>> getCalendario(
            @PathVariable String desde,
            @PathVariable String hasta) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date datDesde;
        Date datHasta;
        try {
            datDesde = new Date(formatter.parse(desde).getTime());
            datHasta = new Date(formatter.parse(hasta).getTime());
        } catch (ParseException e) {
            throw new BusinessValidationException(ErrorCodes.INVALID_DATE_FORMAT,
                    "El formato de fecha no es válido. Use dd-MM-yyyy.");
        }
        List<AreaCultivo> list = areaCultivoService.findByFechaRecogidaBetween(datDesde, datHasta);
        List<AreaCultivoResponse> result = new ArrayList<>();
        list.forEach(ac -> result.add(AreaCultivoResponse.map(ac)));
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/findByActivo/{activo}")
    public ResponseEntity<List<AreaCultivoResponse>> findByActivo(@PathVariable boolean activo){
        List<AreaCultivo> list = areaCultivoService.findByActivo(activo);
        List<AreaCultivoResponse> result = new ArrayList<>();
        list.forEach(ac -> result.add(AreaCultivoResponse.map(ac)));
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/rendimiento")
    public ResponseEntity<List<RendimientoResponse>> calcularRendimiento(){
        return ResponseEntity.ok(areaCultivoService.calcularRendimiento());
    }

    @GetMapping(path = "/excel/calendario/{desde}/{hasta}")
    public ResponseEntity<byte[]> excelCalendario(
            @PathVariable String desde, @PathVariable String hasta) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date datDesde = new Date(formatter.parse(desde).getTime());
            Date datHasta = new Date(formatter.parse(hasta).getTime());
            List<AreaCultivo> list = areaCultivoService.findByFechaRecogidaBetween(datDesde, datHasta);
            byte[] bytes = excelExportService.generarExcelCalendarioCosecha(list);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment", "planificacion_cosecha.xlsx");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return ResponseEntity.ok().headers(headers).body(bytes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/export/csv")
    public ResponseEntity<byte[]> exportCsv(){
        List<AreaCultivo> list = areaCultivoService.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("AreaId,CultivoId,FechaSiembra,FechaRecogida,PlanProd,ProduccionReal,Activo\n");
        for (AreaCultivo ac : list) {
            sb.append(ac.getAreaCultivoPk().getAreaId()).append(",")
              .append(ac.getAreaCultivoPk().getCultivoId()).append(",")
              .append(DateFormatter.format(ac.getAreaCultivoPk().getFechaSiembra())).append(",")
              .append(DateFormatter.format(ac.getFechaRecogida())).append(",")
              .append(ac.getPlanProd() != null ? ac.getPlanProd() : "").append(",")
              .append(ac.getProduccionReal() != null ? ac.getProduccionReal() : "").append(",")
              .append(ac.isActivo()).append("\n");
        }
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "area_cultivos.csv");
        return ResponseEntity.ok().headers(headers).body(bytes);
    }
}
