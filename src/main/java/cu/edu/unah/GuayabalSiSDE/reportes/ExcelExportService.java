package cu.edu.unah.GuayabalSiSDE.reportes;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.util.AgroquimicoReporteResponse;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponseReport;
import cu.edu.unah.GuayabalSiSDE.util.DateFormatter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ExcelExportService {

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public byte[] generarExcelAreasCultivo(List<AreaCultivoResponseReport> datos,
                                            String titulo, String filtro) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Reporte");
            int r = 0;

            // Título
            Row tr = sheet.createRow(r++);
            Cell tc = tr.createCell(0);
            tc.setCellValue(titulo);
            tc.setCellStyle(titleStyle(wb));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

            // Info y fecha
            sheet.createRow(r++).createCell(0).setCellValue(filtro);
            sheet.createRow(r++).createCell(0)
                    .setCellValue("Generado el: " + DATE_FMT.format(new java.util.Date()));
            r++;

            // Cabeceras
            String[] heads = {"Área", "Cultivo", "F. Siembra", "F. Recogida",
                    "Plan (t)", "Real (t)", "Perm. (t)", "Temp. (t)", "Agroquímicos"};
            Row hr = sheet.createRow(r++);
            for (int i = 0; i < heads.length; i++) {
                Cell c = hr.createCell(i);
                c.setCellValue(heads[i]);
                c.setCellStyle(headerStyle(wb));
            }

            // Datos
            for (AreaCultivoResponseReport ac : datos) {
                Row row = sheet.createRow(r++);
                str(row, 0, ac.getArea(), dataStyle(wb));
                str(row, 1, ac.getCultivo(), dataStyle(wb));
                str(row, 2, ac.getFechaSiembra(), dataStyle(wb));
                str(row, 3, ac.getFechaRecogida(), dataStyle(wb));
                num(row, 4, ac.getPlanProd() != null ? ac.getPlanProd().doubleValue() : null, numStyle(wb));
                num(row, 5, ac.getProduccionReal(), numStyle(wb));
                num(row, 6, ac.getProdCultivosPermanente(), numStyle(wb));
                num(row, 7, ac.getProdCultivosTemporales(), numStyle(wb));
                str(row, 8, ac.getAgroquimicos(), dataStyle(wb));
            }

            for (int i = 0; i < heads.length; i++) sheet.autoSizeColumn(i);
            wb.write(out);
            return out.toByteArray();
        }
    }

    public byte[] generarExcelAgroquimicos(List<AgroquimicoReporteResponse> datos) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Agroquímicos");
            int r = 0;

            Row tr = sheet.createRow(r++);
            Cell tc = tr.createCell(0);
            tc.setCellValue("AGROQUÍMICOS MÁS UTILIZADOS");
            tc.setCellStyle(titleStyle(wb));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

            sheet.createRow(r++).createCell(0)
                    .setCellValue("Generado el: " + DATE_FMT.format(new java.util.Date()));
            r++;

            String[] heads = {"ID", "Nombre", "Total Cultivos"};
            Row hr = sheet.createRow(r++);
            for (int i = 0; i < heads.length; i++) {
                Cell c = hr.createCell(i);
                c.setCellValue(heads[i]);
                c.setCellStyle(headerStyle(wb));
            }

            for (AgroquimicoReporteResponse ag : datos) {
                Row row = sheet.createRow(r++);
                num(row, 0, ag.getId() != null ? ag.getId().doubleValue() : null, numStyle(wb));
                str(row, 1, ag.getNombre(), dataStyle(wb));
                num(row, 2, (double) ag.getTotalCultivos(), numStyle(wb));
            }

            for (int i = 0; i < heads.length; i++) sheet.autoSizeColumn(i);
            wb.write(out);
            return out.toByteArray();
        }
    }

    public byte[] generarExcelCalendarioCosecha(List<AreaCultivo> datos) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Cosecha");
            int r = 0;

            Row tr = sheet.createRow(r++);
            Cell tc = tr.createCell(0);
            tc.setCellValue("PLANIFICACIÓN DE COSECHA");
            tc.setCellStyle(titleStyle(wb));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

            sheet.createRow(r++).createCell(0)
                    .setCellValue("Generado el: " + DATE_FMT.format(new java.util.Date()));
            r++;

            String[] heads = {"Estado", "Área", "Cultivo", "F. Siembra", "F. Cosecha", "Plan Prod.", "Prod. Real"};
            Row hr = sheet.createRow(r++);
            for (int i = 0; i < heads.length; i++) {
                Cell c = hr.createCell(i);
                c.setCellValue(heads[i]);
                c.setCellStyle(headerStyle(wb));
            }

            java.sql.Date hoy = new java.sql.Date(System.currentTimeMillis());
            for (AreaCultivo ac : datos) {
                if (ac.getFechaRecogida() == null) continue;
                String estado;
                if (ac.getProduccionReal() != null && ac.getProduccionReal() > 0) {
                    estado = "Completada";
                } else if (ac.isActivo() && ac.getFechaRecogida().before(hoy)) {
                    estado = "Vencida";
                } else {
                    estado = "Planificada";
                }
                Row row = sheet.createRow(r++);
                str(row, 0, estado, dataStyle(wb));
                str(row, 1, ac.getArea() != null ? ac.getArea().getDescripcion() : "", dataStyle(wb));
                str(row, 2, ac.getCultivo() != null ? ac.getCultivo().getDescripcion() : "", dataStyle(wb));
                str(row, 3, DateFormatter.format(ac.getAreaCultivoPk().getFechaSiembra()), dataStyle(wb));
                str(row, 4, DateFormatter.format(ac.getFechaRecogida()), dataStyle(wb));
                num(row, 5, ac.getPlanProd() != null ? ac.getPlanProd().doubleValue() : null, numStyle(wb));
                num(row, 6, ac.getProduccionReal(), numStyle(wb));
            }

            for (int i = 0; i < heads.length; i++) sheet.autoSizeColumn(i);
            wb.write(out);
            return out.toByteArray();
        }
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private void str(Row row, int col, String val, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(val != null ? val : "");
        c.setCellStyle(style);
    }

    private void num(Row row, int col, Double val, CellStyle style) {
        Cell c = row.createCell(col);
        if (val != null) c.setCellValue(val);
        c.setCellStyle(style);
    }

    private CellStyle titleStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        f.setFontHeightInPoints((short) 13);
        s.setFont(f);
        return s;
    }

    private CellStyle headerStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBold(true);
        f.setColor(IndexedColors.WHITE.getIndex());
        s.setFont(f);
        s.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setBorderBottom(BorderStyle.THIN);
        s.setAlignment(HorizontalAlignment.CENTER);
        return s;
    }

    private CellStyle dataStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);
        return s;
    }

    private CellStyle numStyle(Workbook wb) {
        CellStyle s = wb.createCellStyle();
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);
        s.setAlignment(HorizontalAlignment.RIGHT);
        return s;
    }
}
