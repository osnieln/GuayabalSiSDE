package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Trabajador;
import cu.edu.unah.GuayabalSiSDE.repository.TrabajadorRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrabajadorServiceImpl implements TrabajadorService {

    @Autowired
    private final TrabajadorRepository trabajadorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Trabajador> findAll() {
        return trabajadorRepository.findAll(Sort.by("id"));
    }

    @Override
    @Transactional(readOnly = true)
    public Trabajador findById(Long id) {
        return trabajadorRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Trabajador create(Trabajador trabajador) {
        if (trabajador.getNombre() == null || trabajador.getNombre().isBlank())
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El nombre del trabajador es obligatorio.");
        if (trabajador.getIdentificacion() == null || trabajador.getIdentificacion().isBlank())
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "La identificación del trabajador es obligatoria.");
        if (!trabajador.getIdentificacion().matches("^[0-9]{11}$"))
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "La identificación debe tener exactamente 11 dígitos.");
        if (trabajador.getCargo() == null || trabajador.getCargo().isBlank())
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El cargo del trabajador es obligatorio.");
        Trabajador trabajadorDb = trabajadorRepository.findByIdentificacion(trabajador.getIdentificacion());
        if (trabajadorDb != null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "Ya existe un trabajador con la identificación \"" + trabajador.getIdentificacion() + "\".");
        return trabajadorRepository.save(trabajador);
    }

    @Override
    @Transactional
    public Trabajador edit(Trabajador trabajador) {
        if (trabajador.getId() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El ID del trabajador es obligatorio para editarlo.");
        if (trabajador.getNombre() == null || trabajador.getNombre().isBlank())
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El nombre del trabajador es obligatorio.");
        if (trabajador.getIdentificacion() == null || trabajador.getIdentificacion().isBlank())
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "La identificación del trabajador es obligatoria.");
        if (!trabajador.getIdentificacion().matches("^[0-9]{11}$"))
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "La identificación debe tener exactamente 11 dígitos.");
        if (trabajador.getCargo() == null || trabajador.getCargo().isBlank())
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El cargo del trabajador es obligatorio.");
        Trabajador trabajadorDb = findById(trabajador.getId());
        if (trabajadorDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el trabajador con ID " + trabajador.getId() + ".");
        trabajadorDb.setNombre(trabajador.getNombre());
        trabajadorDb.setIdentificacion(trabajador.getIdentificacion());
        trabajadorDb.setCargo(trabajador.getCargo());
        trabajadorDb.setTelefono(trabajador.getTelefono());
        trabajadorDb.setActivo(trabajador.isActivo());
        trabajadorDb.setAreas(trabajador.getAreas());
        trabajadorDb.setRiegos(trabajador.getRiegos());
        return trabajadorRepository.save(trabajadorDb);
    }

    @Override
    @Transactional
    public Trabajador delete(Long id) {
        Trabajador trabajadorDb = findById(id);
        if (trabajadorDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el trabajador con ID " + id + ".");
        trabajadorRepository.delete(trabajadorDb);
        return trabajadorDb;
    }
}
