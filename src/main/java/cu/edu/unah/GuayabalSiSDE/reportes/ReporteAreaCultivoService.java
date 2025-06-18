package cu.edu.unah.GuayabalSiSDE.reportes;

import cu.edu.unah.GuayabalSiSDE.controller.AreaCultivoController;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponse;
import cu.edu.unah.GuayabalSiSDE.util.AreaCultivoResponseReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JRParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteAreaCultivoService {

    @Autowired
    private AreaCultivoController areaCultivoController;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public byte[] exportReportByPlanProdBetween(Long planProdInicio, Long planProdFin) throws FileNotFoundException, JRException {
        // Obtener los datos del controlador
        List<AreaCultivoResponseReport> areasCultivo = areaCultivoController
                .findAreaCultivoByPlanProdBetween(planProdInicio, planProdFin)
                .getBody();

        if (areasCultivo == null || areasCultivo.isEmpty()) {
            throw new RuntimeException("No se encontraron áreas de cultivo para el rango de producción especificado");
        }

        // Cargar el archivo .jrxml
        File file = ResourceUtils.getFile("classpath:reportes/area_cultivo_plan_prod.jrxml");
        
        // Compilar el reporte
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        
        // Crear una lista de mapas con los datos formateados
        List<Map<String, Object>> areasCultivoData = areasCultivo.stream()
                .map(this::crearDatosAreaCultivo)
                .collect(Collectors.toList());
        
        // Crear el datasource a partir de la lista
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(areasCultivoData);
        
        // Parámetros del reporte
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

    public byte[] exportReportFindByProdCultivosPermanenteAfter(Double prodCultivosPermanente) throws FileNotFoundException, JRException {
        // Obtener los datos del controlador
        List<AreaCultivoResponseReport> areasCultivo = areaCultivoController
                .findAreaCultivoByProdCultivosPermanenteAfter(prodCultivosPermanente)
                .getBody();

        if (areasCultivo == null || areasCultivo.isEmpty()) {
            throw new RuntimeException("No se encontraron áreas de cultivo para el rango de producción especificado");
        }

        // Cargar el archivo .jrxml
        File file = ResourceUtils.getFile("classpath:reportes/area_cultivo_plan_prod.jrxml");

        // Compilar el reporte
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Crear una lista de mapas con los datos formateados
        List<Map<String, Object>> areasCultivoData = areasCultivo.stream()
                .map(this::crearDatosAreaCultivo)
                .collect(Collectors.toList());

        // Crear el datasource a partir de la lista
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(areasCultivoData);

        // Parámetros del reporte
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

    public byte[] exportReportFindByFechaRecogidaBefore(String fechaRecogida) throws FileNotFoundException, JRException {
        // Obtener los datos del controlador
        List<AreaCultivoResponseReport> areasCultivo = areaCultivoController
                .findAreaCultivoByFechaRecogidaBefore(fechaRecogida)
                .getBody();

        if (areasCultivo == null || areasCultivo.isEmpty()) {
            throw new RuntimeException("No se encontraron áreas de cultivo para el rango de producción especificado");
        }

        // Cargar el archivo .jrxml
        File file = ResourceUtils.getFile("classpath:reportes/area_cultivo_plan_prod.jrxml");

        // Compilar el reporte
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Crear una lista de mapas con los datos formateados
        List<Map<String, Object>> areasCultivoData = areasCultivo.stream()
                .map(this::crearDatosAreaCultivo)
                .collect(Collectors.toList());

        // Crear el datasource a partir de la lista
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(areasCultivoData);

        // Parámetros del reporte
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
        
        // Agroquímicos como string separado por comas
        String agroquimicos = !areaCultivo.getAgroquimicos().isEmpty() ?
                String.join(", ", areaCultivo.getAgroquimicos()) : "Ninguno";
        datos.put("agroquimicos", agroquimicos);
        
        // ID único para agrupación
        datos.put("grupoId", areaCultivo.getArea() + "_" +
                            areaCultivo.getCultivo());
        
        return datos;
    }
}
