package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Area;
import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.repository.ProduccionRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduccionServiceImple implements ProduccionService{

    @Autowired
    ProduccionRepository produccionRepository;

    @Override
    public List<Produccion> findAll() { return produccionRepository.findAll(); }

    @Override
    public Produccion findById(Long id) { return produccionRepository.findById(id).orElse(null); }

    @Override
    public Produccion create(@NonNull Produccion produccion) {
        if (produccion.getDescripcion() == null || produccion.getDescripcion().isBlank())
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "La descripción de la producción es obligatoria.");
        return produccionRepository.save(produccion);
    }

    @Override
    public Produccion edit(@NonNull Produccion produccion) {
        if (produccion.getId() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El ID de la producción es obligatorio para editarla.");
        Produccion produccionDb = findById(produccion.getId());
        if (null == produccionDb)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró la producción con ID " + produccion.getId() + ".");
        produccionDb.setDescripcion(produccion.getDescripcion());
        return produccionRepository.save(produccionDb);
    }

    @Override
    public Produccion delete(Long id) {
        Produccion produccionDb = findById(id);
        if (produccionDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró la producción con ID " + id + ".");
        produccionRepository.delete(produccionDb);
        return produccionDb;
    }
}
