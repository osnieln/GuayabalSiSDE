package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.services.PlanificacionCosechaService;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponsePK;
import cu.edu.unah.GuayabalSiSDE.util.CompletarCosechaRequest;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import cu.edu.unah.GuayabalSiSDE.util.PlanificacionCosechaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping(path = "planificacionCosecha")
public class PlanificacionCosechaController {

    @Autowired
    private PlanificacionCosechaService planificacionCosechaService;

    @GetMapping(path = "/calendario/{desde}/{hasta}")
    public ResponseEntity<List<PlanificacionCosechaResponse>> calendario(
            @PathVariable String desde, @PathVariable String hasta) {
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
        return ResponseEntity.ok(planificacionCosechaService.findCalendario(datDesde, datHasta));
    }

    @PostMapping(path = "/completar")
    public ResponseEntity<PlanificacionCosechaResponse> completar(@RequestBody CompletarCosechaRequest request) {
        AreaCultivoPk pk = AreaCultivoResponsePK.map(request.getAreaCultivoResponsePk());
        return ResponseEntity.ok(planificacionCosechaService.completar(pk, request.getProduccionReal()));
    }

    @PostMapping(path = "/cancelar")
    public ResponseEntity<PlanificacionCosechaResponse> cancelar(@RequestBody AreaCultivoResponsePK areaCultivoResponsePK) {
        AreaCultivoPk pk = AreaCultivoResponsePK.map(areaCultivoResponsePK);
        return ResponseEntity.ok(planificacionCosechaService.cancelar(pk));
    }
}
