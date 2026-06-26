package cu.edu.unah.GuayabalSiSDE.controller;

import cu.edu.unah.GuayabalSiSDE.reportes.EmailService;
import cu.edu.unah.GuayabalSiSDE.reportes.ExcelExportService;
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
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import java.io.PrintWriter;
import java.io.StringWriter;
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

    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private EmailService emailService;

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

        } catch (Throwable e) {
            System.err.println("[ReporteController] ERROR generando reporte: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().contentType(MediaType.TEXT_PLAIN).body(("ERROR: " + e.getClass().getName() + ": " + e.getMessage()).getBytes());
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

        } catch (Throwable e) {
            System.err.println("[ReporteController] ERROR generando reporte: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().contentType(MediaType.TEXT_PLAIN).body(("ERROR: " + e.getClass().getName() + ": " + e.getMessage()).getBytes());
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

        } catch (Throwable e) {
            System.err.println("[ReporteController] ERROR generando reporte: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().contentType(MediaType.TEXT_PLAIN).body(("ERROR: " + e.getClass().getName() + ": " + e.getMessage()).getBytes());
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

        } catch (Throwable e) {
            System.err.println("[ReporteController] ERROR generando reporte: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().contentType(MediaType.TEXT_PLAIN).body(("ERROR: " + e.getClass().getName() + ": " + e.getMessage()).getBytes());
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

        } catch (Throwable e) {
            System.err.println("[ReporteController] ERROR generando reporte: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().contentType(MediaType.TEXT_PLAIN).body(("ERROR: " + e.getClass().getName() + ": " + e.getMessage()).getBytes());
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
        } catch (Throwable e) {
            System.err.println("[ReporteController] ERROR generando reporte: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().contentType(MediaType.TEXT_PLAIN).body(("ERROR: " + e.getClass().getName() + ": " + e.getMessage()).getBytes());
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
        } catch (Throwable e) {
            System.err.println("[ReporteController] ERROR generando reporte: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().contentType(MediaType.TEXT_PLAIN).body(("ERROR: " + e.getClass().getName() + ": " + e.getMessage()).getBytes());
        }
    }

    // ── Endpoints Excel ───────────────────────────────────────────────────────

    private static final MediaType EXCEL_MEDIA_TYPE =
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private ResponseEntity<byte[]> excelResponse(byte[] bytes, String filename) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(EXCEL_MEDIA_TYPE);
        h.setContentDispositionFormData("attachment", filename);
        h.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return ResponseEntity.ok().headers(h).body(bytes);
    }

    private ResponseEntity<Map<String, String>> emailOk(String destinatario) {
        Map<String, String> r = new LinkedHashMap<>();
        r.put("status", "ok");
        r.put("mensaje", "Reporte enviado correctamente a " + destinatario);
        return ResponseEntity.ok(r);
    }

    private ResponseEntity<Map<String, String>> emailErr(String msg) {
        Map<String, String> r = new LinkedHashMap<>();
        r.put("status", "error");
        r.put("mensaje", msg);
        return ResponseEntity.internalServerError().body(r);
    }

    @GetMapping("/excel/todasAreasCultivo")
    public ResponseEntity<byte[]> excelTodasAreasCultivo() {
        try {
            List<AreaCultivoResponseReport> data = areaCultivoService.findAll().stream()
                    .map(AreaCultivoResponseReport::map).collect(Collectors.toList());
            byte[] bytes = excelExportService.generarExcelAreasCultivo(data,
                    "RESUMEN GENERAL DE ÁREAS DE CULTIVO", "Sin filtros — todos los registros");
            return excelResponse(bytes, "resumen_areas_cultivo.xlsx");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/excel/planProdBetween/{planInicio}/{planFin}")
    public ResponseEntity<byte[]> excelPlanProd(@PathVariable Long planInicio, @PathVariable Long planFin) {
        try {
            List<AreaCultivoResponseReport> data = areaCultivoService
                    .findAreaCultivoByPlanProdBetween(planInicio, planFin).stream()
                    .map(AreaCultivoResponseReport::map).collect(Collectors.toList());
            byte[] bytes = excelExportService.generarExcelAreasCultivo(data,
                    "ÁREAS DE CULTIVO — POR PLAN DE PRODUCCIÓN",
                    "Plan entre " + planInicio + " t y " + planFin + " t");
            return excelResponse(bytes, "cultivos_plan_produccion.xlsx");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/excel/prodCultivosPermanenteAfter/{prod}")
    public ResponseEntity<byte[]> excelProdPermanente(@PathVariable Double prod) {
        try {
            List<AreaCultivoResponseReport> data = areaCultivoService
                    .findAreaCultivoByProdCultivosPermanenteAfter(prod).stream()
                    .map(AreaCultivoResponseReport::map).collect(Collectors.toList());
            byte[] bytes = excelExportService.generarExcelAreasCultivo(data,
                    "ÁREAS DE CULTIVO — PRODUCCIÓN PERMANENTE",
                    "Producción permanente mayor de " + prod + " t");
            return excelResponse(bytes, "cultivos_prod_permanente.xlsx");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/excel/fechaRecogidaBefore/{fecha}")
    public ResponseEntity<byte[]> excelFechaRecogida(@PathVariable String fecha) {
        try {
            Date d = inputFormat.parse(fecha);
            List<AreaCultivoResponseReport> data = areaCultivoService
                    .findAreaCultivoByFechaRecogidaBefore(new java.sql.Date(d.getTime())).stream()
                    .map(AreaCultivoResponseReport::map).collect(Collectors.toList());
            byte[] bytes = excelExportService.generarExcelAreasCultivo(data,
                    "ÁREAS DE CULTIVO — FILTRO POR FECHA DE RECOGIDA",
                    "Fecha de recogida anterior a: " + fecha);
            return excelResponse(bytes, "cultivos_fecha_recogida.xlsx");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/excel/cultivosPorVencer/{dias}")
    public ResponseEntity<byte[]> excelCultivosPorVencer(@PathVariable int dias) {
        try {
            List<AreaCultivoResponseReport> data = areaCultivoService.findCultivosPorVencer(dias).stream()
                    .map(AreaCultivoResponseReport::map).collect(Collectors.toList());
            byte[] bytes = excelExportService.generarExcelAreasCultivo(data,
                    "CULTIVOS PRÓXIMOS A VENCER",
                    "Próximos " + dias + " día(s)");
            return excelResponse(bytes, "cultivos_por_vencer.xlsx");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/excel/agroquimicosMasUsados")
    public ResponseEntity<byte[]> excelAgroquimicos() {
        try {
            List<AgroquimicoReporteResponse> data = agroquimicoService.findMasUtilizadosConConteo();
            byte[] bytes = excelExportService.generarExcelAgroquimicos(data);
            return excelResponse(bytes, "agroquimicos_mas_usados.xlsx");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ── Endpoints Correo ──────────────────────────────────────────────────────

    @PostMapping("/email/todasAreasCultivo")
    public ResponseEntity<Map<String, String>> emailTodasAreasCultivo(@RequestParam String destinatario) {
        try {
            List<AreaCultivoResponseReport> data = areaCultivoService.findAll().stream()
                    .map(AreaCultivoResponseReport::map).collect(Collectors.toList());
            byte[] bytes = excelExportService.generarExcelAreasCultivo(data,
                    "RESUMEN GENERAL DE ÁREAS DE CULTIVO", "Sin filtros — todos los registros");
            emailService.enviarExcel(destinatario, "Resumen Áreas de Cultivo", bytes, "resumen_areas_cultivo.xlsx");
            return emailOk(destinatario);
        } catch (Exception e) {
            return emailErr(e.getMessage());
        }
    }

    @PostMapping("/email/planProdBetween/{planInicio}/{planFin}")
    public ResponseEntity<Map<String, String>> emailPlanProd(
            @PathVariable Long planInicio, @PathVariable Long planFin,
            @RequestParam String destinatario) {
        try {
            List<AreaCultivoResponseReport> data = areaCultivoService
                    .findAreaCultivoByPlanProdBetween(planInicio, planFin).stream()
                    .map(AreaCultivoResponseReport::map).collect(Collectors.toList());
            byte[] bytes = excelExportService.generarExcelAreasCultivo(data,
                    "ÁREAS DE CULTIVO — POR PLAN DE PRODUCCIÓN",
                    "Plan entre " + planInicio + " t y " + planFin + " t");
            emailService.enviarExcel(destinatario, "Reporte Plan de Producción", bytes, "cultivos_plan_produccion.xlsx");
            return emailOk(destinatario);
        } catch (Exception e) {
            return emailErr(e.getMessage());
        }
    }

    @PostMapping("/email/prodCultivosPermanenteAfter/{prod}")
    public ResponseEntity<Map<String, String>> emailProdPermanente(
            @PathVariable Double prod, @RequestParam String destinatario) {
        try {
            List<AreaCultivoResponseReport> data = areaCultivoService
                    .findAreaCultivoByProdCultivosPermanenteAfter(prod).stream()
                    .map(AreaCultivoResponseReport::map).collect(Collectors.toList());
            byte[] bytes = excelExportService.generarExcelAreasCultivo(data,
                    "ÁREAS DE CULTIVO — PRODUCCIÓN PERMANENTE",
                    "Producción permanente mayor de " + prod + " t");
            emailService.enviarExcel(destinatario, "Reporte Cultivos Permanentes", bytes, "cultivos_prod_permanente.xlsx");
            return emailOk(destinatario);
        } catch (Exception e) {
            return emailErr(e.getMessage());
        }
    }

    @PostMapping("/email/fechaRecogidaBefore/{fecha}")
    public ResponseEntity<Map<String, String>> emailFechaRecogida(
            @PathVariable String fecha, @RequestParam String destinatario) {
        try {
            Date d = inputFormat.parse(fecha);
            List<AreaCultivoResponseReport> data = areaCultivoService
                    .findAreaCultivoByFechaRecogidaBefore(new java.sql.Date(d.getTime())).stream()
                    .map(AreaCultivoResponseReport::map).collect(Collectors.toList());
            byte[] bytes = excelExportService.generarExcelAreasCultivo(data,
                    "ÁREAS DE CULTIVO — FILTRO POR FECHA DE RECOGIDA",
                    "Fecha de recogida anterior a: " + fecha);
            emailService.enviarExcel(destinatario, "Reporte Fecha de Recogida", bytes, "cultivos_fecha_recogida.xlsx");
            return emailOk(destinatario);
        } catch (Exception e) {
            return emailErr(e.getMessage());
        }
    }

    @PostMapping("/email/cultivosPorVencer/{dias}")
    public ResponseEntity<Map<String, String>> emailCultivosPorVencer(
            @PathVariable int dias, @RequestParam String destinatario) {
        try {
            List<AreaCultivoResponseReport> data = areaCultivoService.findCultivosPorVencer(dias).stream()
                    .map(AreaCultivoResponseReport::map).collect(Collectors.toList());
            byte[] bytes = excelExportService.generarExcelAreasCultivo(data,
                    "CULTIVOS PRÓXIMOS A VENCER", "Próximos " + dias + " día(s)");
            emailService.enviarExcel(destinatario, "Reporte Cultivos por Vencer", bytes, "cultivos_por_vencer.xlsx");
            return emailOk(destinatario);
        } catch (Exception e) {
            return emailErr(e.getMessage());
        }
    }

    @PostMapping("/email/agroquimicosMasUsados")
    public ResponseEntity<Map<String, String>> emailAgroquimicos(@RequestParam String destinatario) {
        try {
            List<AgroquimicoReporteResponse> data = agroquimicoService.findMasUtilizadosConConteo();
            byte[] bytes = excelExportService.generarExcelAgroquimicos(data);
            emailService.enviarExcel(destinatario, "Agroquímicos Más Utilizados", bytes, "agroquimicos_mas_usados.xlsx");
            return emailOk(destinatario);
        } catch (Exception e) {
            return emailErr(e.getMessage());
        }
    }

    // ── Endpoints de diagnóstico ──────────────────────────────────────────────

    /**
     * Diagnóstico completo: acceder desde el navegador con
     * http://localhost:8081/api/reportes/diagnostico
     * Devuelve texto plano indicando en qué paso falla la generación del PDF.
     */
    @GetMapping(value = "/diagnostico", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> diagnostico() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DIAGNÓSTICO DE REPORTES ===\n");
        sb.append("Fecha: ").append(new Date()).append("\n\n");

        // 1. JRXML en classpath
        try {
            ClassPathResource r = new ClassPathResource("reportes/area_cultivo_plan_prod.jrxml");
            sb.append("1. JRXML en classpath: ").append(r.exists() ? "ENCONTRADO (" + r.getURL() + ")" : "NO ENCONTRADO").append("\n");
        } catch (Exception e) {
            sb.append("1. JRXML en classpath ERROR: ").append(e).append("\n");
        }

        // 2. Compilación del JRXML
        try {
            ClassPathResource r = new ClassPathResource("reportes/area_cultivo_plan_prod.jrxml");
            net.sf.jasperreports.engine.JasperReport jr =
                    net.sf.jasperreports.engine.JasperCompileManager.compileReport(r.getInputStream());
            sb.append("2. Compilación JRXML: OK (").append(jr.getName()).append(")\n");
        } catch (Exception e) {
            sb.append("2. Compilación JRXML ERROR: ").append(e.getClass().getName()).append(": ").append(e.getMessage()).append("\n");
            if (e.getCause() != null) sb.append("   Caused by: ").append(e.getCause()).append("\n");
        }

        // 3. Consulta a la base de datos
        int registros = 0;
        try {
            List<?> todos = areaCultivoService.findAll();
            registros = todos != null ? todos.size() : 0;
            sb.append("3. Consulta BD: OK (").append(registros).append(" registros)\n");
        } catch (Exception e) {
            sb.append("3. Consulta BD ERROR: ").append(e.getClass().getName()).append(": ").append(e.getMessage()).append("\n");
        }

        // 4. Generación completa del PDF
        try {
            byte[] pdf = reporteAreaCultivoService.exportReportAllAreasCultivo();
            sb.append("4. Generación PDF: OK (").append(pdf.length).append(" bytes)\n");
            sb.append("\n✓ TODO OK — el PDF se genera correctamente.\n");
        } catch (Throwable e) {
            sb.append("4. Generación PDF ERROR: ").append(e.getClass().getName()).append(": ").append(e.getMessage()).append("\n");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            sb.append(sw).append("\n");
            if (e.getCause() != null) {
                sb.append("Caused by: ").append(e.getCause().getClass().getName()).append(": ").append(e.getCause().getMessage()).append("\n");
            }
        }

        return ResponseEntity.ok(sb.toString());
    }

    /**
     * Prueba OpenPDF directamente sin JasperReports.
     * Si este endpoint devuelve un PDF válido pero /todasAreasCultivo falla,
     * el problema es específico de JasperReports.
     * Acceder: http://localhost:8081/api/reportes/testPdf
     */
    @GetMapping("/testPdf")
    public ResponseEntity<byte[]> testPdf() {
        try {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            com.lowagie.text.Document doc = new com.lowagie.text.Document(com.lowagie.text.PageSize.A4);
            com.lowagie.text.pdf.PdfWriter.getInstance(doc, baos);
            doc.open();
            doc.add(new com.lowagie.text.Paragraph("Test PDF - OpenPDF funciona correctamente"));
            doc.add(new com.lowagie.text.Paragraph("Fecha: " + new Date()));
            doc.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "test.pdf");
            return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
        } catch (Throwable e) {
            System.err.println("[ReporteController] ERROR en testPdf: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(("ERROR: " + e.getClass().getName() + ": " + e.getMessage()).getBytes());
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
        return ResponseEntity.ok(agroquimicoService.findMasUtilizadosConConteo());
    }
}
