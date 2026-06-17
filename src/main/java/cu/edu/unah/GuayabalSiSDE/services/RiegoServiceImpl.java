package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivoPk;
import cu.edu.unah.GuayabalSiSDE.entity.Riego;
import cu.edu.unah.GuayabalSiSDE.repository.RiegoRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiegoServiceImpl implements RiegoService {

    @Autowired
    private final RiegoRepository riegoRepository;

    @Autowired
    @Lazy
    private final AreaCultivoService areaCultivoService;


    @Override
    public List<Riego> findAll() {
        return riegoRepository.findAll();
    }

    @Override
    public Riego findById(long id) {
        return riegoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Riego> findByAreaCultivoPk(AreaCultivoPk areaCultivoPk) {
        return riegoRepository.findRiegoByAreaCultivo_AreaCultivoPk(areaCultivoPk);
    }

    @Override
    public Riego create(Riego riego) {
        if (riego.getFechaPlanificacion() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "La fecha planificada del riego es obligatoria.");
        if (riego.getAreaCultivo() == null || riego.getAreaCultivo().getAreaCultivoPk() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El área de cultivo del riego es obligatoria.");
        AreaCultivo areaCultivo = areaCultivoService.findById(riego.getAreaCultivo().getAreaCultivoPk());
        if (areaCultivo == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el área de cultivo asociada al riego.");
        riego.setAreaCultivo(areaCultivo);
        return riegoRepository.save(riego);
    }

    @Override
    public Riego edit(Riego riego) {
        if (riego.getId() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El ID del riego es obligatorio para editarlo.");
        Riego riegoDb = findById(riego.getId());
        if (riegoDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el riego con ID " + riego.getId() + ".");
        if (riego.getFechaPlanificacion() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "La fecha planificada del riego es obligatoria.");
        if (riego.getAreaCultivo() == null || riego.getAreaCultivo().getAreaCultivoPk() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El área de cultivo del riego es obligatoria.");
        AreaCultivo areaCultivoDb = areaCultivoService.findById(riego.getAreaCultivo().getAreaCultivoPk());
        if (areaCultivoDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el área de cultivo asociada al riego.");
        riegoDb.setFechaReal(riego.getFechaReal());
        riegoDb.setFechaPlanificacion(riego.getFechaPlanificacion());
        riegoDb.setAreaCultivo(areaCultivoDb);
        return riegoRepository.save(riegoDb);
    }

    @Override
    public Riego delete(long id) {
        Riego riegoDb = findById(id);
        if (riegoDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el riego con ID " + id + ".");
        riegoRepository.delete(riegoDb);
        return riegoDb;
    }

    @Override
    public List<Riego> findRiegosProximos(int dias) {
        Date hoy = Date.valueOf(LocalDate.now());
        Date hasta = Date.valueOf(LocalDate.now().plusDays(dias));
        return riegoRepository.findByFechaPlanificacionBetweenAndFechaRealIsNull(hoy, hasta);
    }

    @Override
    public List<Riego> findHistorialByArea(Long areaId) {
        return riegoRepository.findByAreaCultivo_AreaCultivoPk_AreaId(areaId);
    }

    @Override
    public List<AreaCultivo> findAreasSinRiego(int dias) {
        Date limite = Date.valueOf(LocalDate.now().minusDays(dias));
        return riegoRepository.findAreasSinRiegoDesde(limite);
    }
}
