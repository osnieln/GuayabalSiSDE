package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;
import cu.edu.unah.GuayabalSiSDE.repository.TipoCultivoRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoCultivoServiceImpl implements TipoCultivoService {

    @Autowired
    private final TipoCultivoRepository tipoCultivoRepository;

    @Override
    public List<TipoCultivo> findAll() {
        return tipoCultivoRepository.findAll(Sort.by("id"));
    }

    @Override
    public TipoCultivo findById(Long id) {
        return tipoCultivoRepository.findById(id).orElse(null);
    }

    @Override
    public TipoCultivo findByNombre(String nombre) {
        return tipoCultivoRepository.findByNombre(nombre);
    }

    @Override
    public TipoCultivo create(TipoCultivo tipoCultivo) {
        if (tipoCultivo.getNombre() == null || tipoCultivo.getNombre().isBlank())
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El nombre del tipo de cultivo es obligatorio.");
        TipoCultivo tipoCultivoDb = findByNombre(tipoCultivo.getNombre());
        if (tipoCultivoDb != null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "Ya existe un tipo de cultivo con el nombre \"" + tipoCultivo.getNombre() + "\".");
        return tipoCultivoRepository.save(tipoCultivo);
    }

    @Override
    public TipoCultivo edit(TipoCultivo tipoCultivo) {
        if (tipoCultivo.getId() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El ID del tipo de cultivo es obligatorio para editarlo.");
        TipoCultivo tipoCultivoDb = findById(tipoCultivo.getId());
        if (tipoCultivoDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el tipo de cultivo con ID " + tipoCultivo.getId() + ".");
        tipoCultivoDb.setNombre(tipoCultivo.getNombre());
        return tipoCultivoRepository.save(tipoCultivoDb);
    }

    @Override
    public TipoCultivo delete(Long id) {
        TipoCultivo tipoCultivoDb = findById(id);
        if (tipoCultivoDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el tipo de cultivo con ID " + id + ".");
        tipoCultivoRepository.delete(tipoCultivoDb);
        return tipoCultivoDb;
    }
}
