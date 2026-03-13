package cu.edu.unah.GuayabalSiSDE.reportes;

import cu.edu.unah.GuayabalSiSDE.controller.AreaCultivoController;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.services.AgroquimicoService;
import cu.edu.unah.GuayabalSiSDE.services.AreaCultivoService;
import cu.edu.unah.GuayabalSiSDE.util.AgroquimicoReporteResponse;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponse;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponseReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JRParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteAreaCultivoService {

    @Autowired
    private AreaCultivoController areaCultivoController;

    @Autowired
    private AreaCultivoService areaCultivoService;

    @Autowired
    private AgroquimicoService agroquimicoService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public byte[] exportReportAllAreasCultivo() throws IOException, JRException {
        List<AreaCultivo> todos = areaCultivoService.findAll();

        if (todos == null || todos.isEmpty()) {
            throw new RuntimeException("No se encontraron áreas de cultivo registradas");
        }

        List<AreaCultivoResponseReport> areasCultivo = todos.stream()
                .map(AreaCultivoResponseReport::map)
                .collect(Collectors.toList());

        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("reportes/area_cultivo_plan_prod.jrxml").getInputStream());

        List<Map<String, Object>> data = areasCultivo.stream()
                .map(this::crearDatosAreaCultivo)
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "RESUMEN GENERAL DE ÁREAS DE CULTIVO");
        parameters.put("GENERATION_DATE", "Generado el: " + dateFormat.format(new Date()));
        parameters.put("TOTAL_REGISTROS", "Total de registros: " + areasCultivo.size());
        parameters.put("RANGO_PLAN", "Sin filtros — todos los registros");
        parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] exportReportByPlanProdBetween(Long planProdInicio, Long planProdFin) throws IOException, JRException {
        // Obtener los datos del controlador
        List<AreaCultivoResponseReport> areasCultivo = areaCultivoController
                .findAreaCultivoByPlanProdBetween(planProdInicio, planProdFin)
                .getBody();

        if (areasCultivo == null) areasCultivo = java.util.Collections.emptyList();

        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("reportes/area_cultivo_plan_prod.jrxml").getInputStream());

        List<Map<String, Object>> areasCultivoData = areasCultivo.stream()
                .map(this::crearDatosAreaCultivo)
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(areasCultivoData);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "INFORME DE ÁREAS DE CULTIVO");
        parameters.put("GENERATION_DATE", "Generado el: " + dateFormat.format(new Date()));
        parameters.put("TOTAL_REGISTROS", "Total de registros: " + areasCultivo.size());
        parameters.put("RANGO_PLAN", "Rango de producción: " + planProdInicio + " - " + planProdFin);
        
        // Configuración adicional para el reporte
        parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
        
        // Llenar el reporte
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        
        // Exportar a PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] exportReportFindByProdCultivosPermanenteAfter(Double prodCultivosPermanente) throws IOException, JRException {
        // Obtener los datos del controlador
        List<AreaCultivoResponseReport> areasCultivo = areaCultivoController
                .findAreaCultivoByProdCultivosPermanenteAfter(prodCultivosPermanente)
                .getBody();

        if (areasCultivo == null) areasCultivo = java.util.Collections.emptyList();

        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("reportes/area_cultivo_plan_prod.jrxml").getInputStream());

        List<Map<String, Object>> areasCultivoData = areasCultivo.stream()
                .map(this::crearDatosAreaCultivo)
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(areasCultivoData);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "INFORME DE ÁREAS DE CULTIVO");
        parameters.put("GENERATION_DATE", "Generado el: " + dateFormat.format(new Date()));
        parameters.put("TOTAL_REGISTROS", "Total de registros: " + areasCultivo.size());
        parameters.put("RANGO_PLAN", "Producción de cultivos permanentes mayor de: " + prodCultivosPermanente);

        // Configuración adicional para el reporte
        parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

        // Llenar el reporte
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Exportar a PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] exportReportFindByFechaRecogidaBefore(String fechaRecogida) throws IOException, JRException {
        // Obtener los datos del controlador
        List<AreaCultivoResponseReport> areasCultivo = areaCultivoController
                .findAreaCultivoByFechaRecogidaBefore(fechaRecogida)
                .getBody();

        if (areasCultivo == null) areasCultivo = java.util.Collections.emptyList();

        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("reportes/area_cultivo_plan_prod.jrxml").getInputStream());

        List<Map<String, Object>> areasCultivoData = areasCultivo.stream()
                .map(this::crearDatosAreaCultivo)
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(areasCultivoData);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "INFORME DE ÁREAS DE CULTIVO");
        parameters.put("GENERATION_DATE", "Generado el: " + dateFormat.format(new Date()));
        parameters.put("TOTAL_REGISTROS", "Total de registros: " + areasCultivo.size());
        parameters.put("RANGO_PLAN", "Fecha de recogida antes de: " + fechaRecogida);

        // Configuración adicional para el reporte
        parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

        // Llenar el reporte
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Exportar a PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private Map<String, Object> crearDatosAreaCultivo(AreaCultivoResponseReport areaCultivo) {
        Map<String, Object> datos = new HashMap<>();
        
        // Datos básicos
        datos.put("areaId", areaCultivo.getArea());
        datos.put("cultivoId", areaCultivo.getCultivo());
        datos.put("fechaSiembra", areaCultivo.getFechaSiembra());
        datos.put("fechaRecogida", areaCultivo.getFechaRecogida());
        datos.put("planProd", areaCultivo.getPlanProd());
        datos.put("prodCultivosPermanente", areaCultivo.getProdCultivosPermanente());
        datos.put("prodCultivosTemporales", areaCultivo.getProdCultivosTemporales());
        datos.put("produccionReal", areaCultivo.getProduccionReal());
        
        datos.put("agroquimicos", areaCultivo.getAgroquimicos());
        
        // ID único para agrupación
        datos.put("grupoId", areaCultivo.getArea() + "_" +
                            areaCultivo.getCultivo());

        return datos;
    }

    public byte[] exportReportCultivosPorVencer(int dias) throws IOException, JRException {
        List<AreaCultivo> cultivos = areaCultivoService.findCultivosPorVencer(dias);

        List<AreaCultivoResponseReport> data = (cultivos != null)
                ? cultivos.stream().map(AreaCultivoResponseReport::map).collect(Collectors.toList())
                : java.util.Collections.emptyList();

        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("reportes/cultivos_por_vencer.jrxml").getInputStream());

        List<Map<String, Object>> rows = data.stream().map(ac -> {
            Map<String, Object> d = new HashMap<>();
            d.put("area", ac.getArea());
            d.put("cultivo", ac.getCultivo());
            d.put("fechaSiembra", ac.getFechaSiembra());
            d.put("fechaRecogida", ac.getFechaRecogida());
            d.put("planProd", ac.getPlanProd());
            d.put("produccionReal", ac.getProduccionReal());
            d.put("agroquimicos", ac.getAgroquimicos());
            return d;
        }).collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rows);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "CULTIVOS PRÓXIMOS A VENCER");
        parameters.put("GENERATION_DATE", "Generado el: " + dateFormat.format(new Date()));
        parameters.put("TOTAL_REGISTROS", "Total de registros: " + data.size());
        parameters.put("FILTRO_DIAS", "Próximos " + dias + " día(s)");
        parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public byte[] exportReportAgroquimicosMasUsados() throws IOException, JRException {
        List<AgroquimicoReporteResponse> agroquimicos = agroquimicoService.findMasUtilizadosConConteo();

        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("reportes/agroquimicos_reporte.jrxml").getInputStream());

        List<Map<String, Object>> rows = agroquimicos.stream().map(ag -> {
            Map<String, Object> d = new HashMap<>();
            d.put("id", ag.getId());
            d.put("nombre", ag.getNombre());
            d.put("totalCultivos", ag.getTotalCultivos());
            return d;
        }).collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rows);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "AGROQUÍMICOS MÁS UTILIZADOS");
        parameters.put("GENERATION_DATE", "Generado el: " + dateFormat.format(new Date()));
        parameters.put("TOTAL_REGISTROS", "Total de agroquímicos: " + agroquimicos.size());
        parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
