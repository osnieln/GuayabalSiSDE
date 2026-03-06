package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.repository.AreaCultivoRepository;
import cu.edu.unah.GuayabalSiSDE.repository.AreaRepository;
import cu.edu.unah.GuayabalSiSDE.repository.RiegoRepository;
import cu.edu.unah.GuayabalSiSDE.util.DashboardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private AreaCultivoRepository areaCultivoRepository;

    @Autowired
    private RiegoRepository riegoRepository;

    @Override
    public DashboardResponse getEstadisticas() {
        long totalAreas = areaRepository.count();

        long totalCultivosActivos = areaCultivoRepository.findByActivo(true).size();

        Date hoy = Date.valueOf(LocalDate.now());
        Date en7Dias = Date.valueOf(LocalDate.now().plusDays(7));
        long proximosRiegos = riegoRepository.findByFechaPlanificacionBetween(hoy, en7Dias).size();

        YearMonth mesActual = YearMonth.now();
        Date inicioMes = Date.valueOf(mesActual.atDay(1));
        Date finMes = Date.valueOf(mesActual.atEndOfMonth());
        long produccionesDelMes = areaCultivoRepository.findAreaCultivoByFechaRecogidaBefore(finMes)
                .stream()
                .filter(ac -> ac.getFechaRecogida() != null && !ac.getFechaRecogida().before(inicioMes))
                .count();

        return DashboardResponse.builder()
                .totalAreas(totalAreas)
                .totalCultivosActivos(totalCultivosActivos)
                .proximosRiegos(proximosRiegos)
                .produccionesDelMes(produccionesDelMes)
                .build();
    }
}
