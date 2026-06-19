package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Trabajador;
import cu.edu.unah.GuayabalSiSDE.repository.TrabajadorRepository;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.BusinessValidationException;
import cu.edu.unah.GuayabalSiSDE.util.ExceptionControl.ErrorCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrabajadorServiceImpl implements TrabajadorService {

    @Autowired
    private final TrabajadorRepository trabajadorRepository;

    @Override
    public List<Trabajador> findAll() {
        return trabajadorRepository.findAll(Sort.by("id"));
    }

    @Override
    public Trabajador findById(Long id) {
        return trabajadorRepository.findById(id).orElse(null);
    }

    @Override
    public Trabajador create(Trabajador trabajador) {
        if (trabajador.getNombre() == null || trabajador.getNombre().isBlank())
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El nombre del trabajador es obligatorio.");
        if (trabajador.getIdentificacion() == null || trabajador.getIdentificacion().isBlank())
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "La identificación del trabajador es obligatoria.");
        Trabajador trabajadorDb = trabajadorRepository.findByIdentificacion(trabajador.getIdentificacion());
        if (trabajadorDb != null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "Ya existe un trabajador con la identificación \"" + trabajador.getIdentificacion() + "\".");
        return trabajadorRepository.save(trabajador);
    }

    @Override
    public Trabajador edit(Trabajador trabajador) {
        if (trabajador.getId() == null)
            throw new BusinessValidationException(ErrorCodes.MISSING_REQUIRED_FIELD, "El ID del trabajador es obligatorio para editarlo.");
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
    public Trabajador delete(Long id) {
        Trabajador trabajadorDb = findById(id);
        if (trabajadorDb == null)
            throw new BusinessValidationException(ErrorCodes.OPERATION_VALIDATION_ERROR, "No se encontró el trabajador con ID " + id + ".");
        trabajadorRepository.delete(trabajadorDb);
        return trabajadorDb;
    }
}
