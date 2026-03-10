package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.entity.Agroquimico;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.reportes.ReporteAreaCultivoService;
import cu.edu.unah.GuayabalSiSDE.reportes.ReporteService;
import cu.edu.unah.GuayabalSiSDE.services.AgroquimicoService;
import cu.edu.unah.GuayabalSiSDE.services.AreaCultivoService;
import cu.edu.unah.GuayabalSiSDE.util.AgroquimicoReporteResponse;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponseReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ReporteAreaCultivoService reporteAreaCultivoService;

    @Autowired
    private AreaCultivoService areaCultivoService;

    @Autowired
    private AgroquimicoService agroquimicoService;

    private final SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");

    @GetMapping("/cultivos")
    public ResponseEntity<byte[]> generarReporteCultivos() {
        try {
            byte[] reporte = reporteService.exportReport();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_cultivos.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reporte);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/todasAreasCultivo")
    public ResponseEntity<byte[]> generarReporteTodasAreasCultivo() {
        try {
            byte[] reporte = reporteAreaCultivoService.exportReportAllAreasCultivo();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_resumen_areas_cultivo.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reporte);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/planProdBetween/{planInicio}/{planFin}")
    public ResponseEntity<byte[]> generarReportePlanProd(
            @PathVariable Long planInicio,
            @PathVariable Long planFin) {
        try {
            byte[] reporte = reporteAreaCultivoService.exportReportByPlanProdBetween(planInicio, planFin);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_areas_cultivo.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reporte);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/prodCultivosPermanenteAfter/{prodCultivosPermanente}")
    public ResponseEntity<byte[]> prodCultivosPermanenteAfter(
            @PathVariable Double prodCultivosPermanente) {
        try {
            byte[] reporte = reporteAreaCultivoService.exportReportFindByProdCultivosPermanenteAfter(prodCultivosPermanente);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_Cultivos_Permanentes_after.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reporte);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/fechaRecogidaBefore/{fechaRecogida}")
    public ResponseEntity<byte[]> fechaRecogidaBefore(
            @PathVariable String fechaRecogida) {
        try {
            byte[] reporte = reporteAreaCultivoService.exportReportFindByFechaRecogidaBefore(fechaRecogida);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "reporte_fecha_recogida_before.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reporte);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/cultivosPorVencer/{dias}")
    public ResponseEntity<byte[]> cultivosPorVencer(@PathVariable int dias) {
        try {
            byte[] reporte = reporteAreaCultivoService.exportReportCultivosPorVencer(dias);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "cultivos_por_vencer.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return ResponseEntity.ok().headers(headers).body(reporte);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/agroquimicosMasUsados")
    public ResponseEntity<byte[]> agroquimicosMasUsados() {
        try {
            byte[] reporte = reporteAreaCultivoService.exportReportAgroquimicosMasUsados();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "agroquimicos_mas_usados.pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return ResponseEntity.ok().headers(headers).body(reporte);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // ── Endpoints JSON para vista previa en pantalla ──────────────────────────

    @GetMapping("/preview/todasAreasCultivo")
    public ResponseEntity<List<AreaCultivoResponseReport>> previewTodasAreasCultivo() {
        List<AreaCultivoResponseReport> data = areaCultivoService.findAll().stream()
                .map(AreaCultivoResponseReport::map).collect(Collectors.toList());
        return ResponseEntity.ok(data);
    }

    @GetMapping("/preview/planProdBetween/{planInicio}/{planFin}")
    public ResponseEntity<List<AreaCultivoResponseReport>> previewPlanProd(
            @PathVariable Long planInicio, @PathVariable Long planFin) {
        List<AreaCultivoResponseReport> data = areaCultivoService.findAreaCultivoByPlanProdBetween(planInicio, planFin)
                .stream().map(AreaCultivoResponseReport::map).collect(Collectors.toList());
        return ResponseEntity.ok(data);
    }

    @GetMapping("/preview/prodCultivosPermanenteAfter/{prod}")
    public ResponseEntity<List<AreaCultivoResponseReport>> previewProdPermanente(@PathVariable Double prod) {
        List<AreaCultivoResponseReport> data = areaCultivoService.findAreaCultivoByProdCultivosPermanenteAfter(prod)
                .stream().map(AreaCultivoResponseReport::map).collect(Collectors.toList());
        return ResponseEntity.ok(data);
    }

    @GetMapping("/preview/fechaRecogidaBefore/{fecha}")
    public ResponseEntity<List<AreaCultivoResponseReport>> previewFechaRecogida(@PathVariable String fecha) {
        try {
            Date d = inputFormat.parse(fecha);
            List<AreaCultivoResponseReport> data = areaCultivoService
                    .findAreaCultivoByFechaRecogidaBefore(new java.sql.Date(d.getTime()))
                    .stream().map(AreaCultivoResponseReport::map).collect(Collectors.toList());
            return ResponseEntity.ok(data);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/preview/cultivosPorVencer/{dias}")
    public ResponseEntity<List<AreaCultivoResponseReport>> previewCultivosPorVencer(@PathVariable int dias) {
        List<AreaCultivoResponseReport> data = areaCultivoService.findCultivosPorVencer(dias)
                .stream().map(AreaCultivoResponseReport::map).collect(Collectors.toList());
        return ResponseEntity.ok(data);
    }

    @GetMapping("/preview/agroquimicosMasUsados")
    public ResponseEntity<List<AgroquimicoReporteResponse>> previewAgroquimicos() {
        List<AgroquimicoReporteResponse> data = agroquimicoService.findMasUtilizados().stream()
                .map(AgroquimicoReporteResponse::map).collect(Collectors.toList());
        return ResponseEntity.ok(data);
    }
}
