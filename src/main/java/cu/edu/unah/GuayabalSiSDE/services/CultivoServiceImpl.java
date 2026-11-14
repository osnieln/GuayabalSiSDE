package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;
import cu.edu.unah.GuayabalSiSDE.repository.CultivoRepository;
import cu.edu.unah.GuayabalSiSDE.repository.ProduccionRepository;
import cu.edu.unah.GuayabalSiSDE.repository.TipoCultivoRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CultivoServiceImpl implements CultivoService{

    @Autowired
    CultivoRepository cultivoRepository;

    @Autowired
    ProduccionRepository produccionRepository;

    @Autowired
    TipoCultivoRepository tipoCultivoRepository;

    String className = "Cultivo";

    @Override
    public List<Cultivo> findAll() {
        return cultivoRepository.findAll();
    }

    @Override
    public Cultivo findById(Long id) {
        return cultivoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Cultivo> findByProduccion(Long idProduccion) {
        Produccion produccionDb = produccionRepository.findById(idProduccion).orElse(null);
        if(null == produccionDb) {
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "Este cultivo no existe.");
        }
        return cultivoRepository.findCultivoByProduccion(produccionDb);
    }

    @Override
    public Cultivo create(@NonNull Cultivo cultivo) {
        if (cultivo.getDescripcion() == null || cultivo.getDescripcion().isBlank())
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "La descripción del cultivo es obligatoria.");
        if (cultivo.getProduccion() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "Debe asociar una producción al cultivo.");
        if (cultivo.getTipoCultivo() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "Debe asociar un tipo de cultivo al cultivo.");
        Produccion produccionDb = produccionRepository.findById(cultivo.getProduccion().getId()).orElse(null);
        if (produccionDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró la producción con ID " + cultivo.getProduccion().getId() + ".");
        TipoCultivo tipoCultivoDb = tipoCultivoRepository.findById(cultivo.getTipoCultivo().getId()).orElse(null);
        if (tipoCultivoDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el tipo de cultivo con ID " + cultivo.getTipoCultivo().getId() + ".");
        cultivo.setProduccion(produccionDb);
        cultivo.setTipoCultivo(tipoCultivoDb);
        return cultivoRepository.save(cultivo);
    }

    @Override
    public Cultivo edit(@NonNull Cultivo cultivo) {
        if (cultivo.getId() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El ID del cultivo es obligatorio para editarlo.");
        Cultivo cultivoDb = findById(cultivo.getId());
        if (null == cultivoDb)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el cultivo con ID " + cultivo.getId() + ".");
        if (cultivo.getDescripcion() == null || cultivo.getDescripcion().isBlank())
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "La descripción del cultivo es obligatoria.");
        cultivoDb.setDescripcion(cultivo.getDescripcion());
        if (cultivo.getProduccion() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "Debe asociar una producción al cultivo.");
        Produccion produccionDb = produccionRepository.findById(cultivo.getProduccion().getId()).orElse(null);
        if (produccionDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró la producción con ID " + cultivo.getProduccion().getId() + ".");
        TipoCultivo tipoCultivoDb = null;
        if (cultivo.getTipoCultivo() != null)
            tipoCultivoDb = tipoCultivoRepository.findById(cultivo.getTipoCultivo().getId()).orElse(null);
        cultivoDb.setProduccion(produccionDb);
        cultivoDb.setTipoCultivo(tipoCultivoDb);
        return cultivoRepository.save(cultivoDb);
    }

    @Override
    public Cultivo delete(Long id) {
        Cultivo cultivoDb = findById(id);
        if (cultivoDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el cultivo con ID " + id + ".");
        cultivoRepository.delete(cultivoDb);
        return cultivoDb;
    }

    @Override
    public List<Cultivo> findByDescripcion(String texto) {
        return cultivoRepository.findByDescripcionContainingIgnoreCase(texto);
    }
}
