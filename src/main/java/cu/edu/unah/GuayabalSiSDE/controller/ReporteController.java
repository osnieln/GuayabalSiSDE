package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.reportes.ReporteAreaCultivoService;
import cu.edu.unah.GuayabalSiSDE.reportes.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private ReporteAreaCultivoService reporteAreaCultivoService;

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
}
