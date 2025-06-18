package cu.edu.unah.GuayabalSiSDE.reportes;

import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JRParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import cu.edu.unah.GuayabalSiSDE.services.CultivoService;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private CultivoService cultivoService;
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public byte[] exportReport() throws FileNotFoundException, JRException {
        // Obtener la lista de cultivos con sus relaciones
        List<Cultivo> cultivos = cultivoService.findAll();
        
        // Cargar el archivo .jrxml
        File file = ResourceUtils.getFile("classpath:reportes/cultivos_nuevo.jrxml");
        
        // Compilar el reporte
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        
        // Crear una lista de mapas con los datos formateados
        List<Map<String, Object>> cultivosData = cultivos.stream()
                .map(this::crearDatosCultivo)
                .collect(Collectors.toList());
        
        // Crear el datasource a partir de la lista de cultivos
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cultivosData);
        
        // Parámetros del reporte
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "INFORME DETALLADO DE CULTIVOS");
        parameters.put("GENERATION_DATE", "Generado el: " + dateFormat.format(new Date()));
        parameters.put("TOTAL_CULTIVOS", "Total de cultivos: " + cultivos.size());
        
        // Configuración adicional para el reporte
        parameters.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
        
        // Llenar el reporte
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        
        // Exportar a PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    
    private Map<String, Object> crearDatosCultivo(Cultivo cultivo) {
        Map<String, Object> datos = new HashMap<>();
        
        // Datos básicos del cultivo
        datos.put("id", cultivo.getId());
        datos.put("descripcion", cultivo.getDescripcion() != null ? cultivo.getDescripcion() : "Sin descripción");
        
        // Datos del tipo de cultivo
        if (cultivo.getTipoCultivo() != null) {
            datos.put("tipoCultivoNombre", cultivo.getTipoCultivo().getNombre());
        } else {
            datos.put("tipoCultivoNombre", "No especificado");
        }
        
        // Datos de producción
        if (cultivo.getProduccion() != null) {
            datos.put("produccionDescripcion", cultivo.getProduccion().getDescripcion());
            datos.put("produccionId", cultivo.getProduccion().getId());
        } else {
            datos.put("produccionDescripcion", "Sin producción asociada");
            datos.put("produccionId", "N/A");
        }
        
        // Agregar un identificador único para agrupar por cultivo en el reporte
        datos.put("grupoId", "cultivo_" + cultivo.getId());
        
        return datos;
    }
}
