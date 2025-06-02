package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.repository.CultivoRepository;
import cu.edu.unah.GuayabalSiSDE.repository.ProduccionRepository;
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
        Produccion produccion = produccionRepository.findById(cultivo.getProduccion().getId()).orElse(null);
        cultivo.setProduccion(produccion);
        return cultivoRepository.save(cultivo);
    }

    @Override
    public Cultivo edit(@NonNull Cultivo cultivo) {
        Cultivo cultivoDb = findById(cultivo.getId());
        if(null == cultivoDb)
            return null;
        cultivoDb.setDescripcion(cultivo.getDescripcion());
        if(null == cultivo.getProduccion())
            return null;
        Produccion produccionDb = produccionRepository.findById(cultivo.getProduccion().getId()).orElse(null);
        if(produccionDb==null)
            return null;
        cultivoDb.setProduccion(produccionDb);
        return cultivoRepository.save(cultivoDb);
    }

    @Override
    public Cultivo delete(Long id) {
        Cultivo cultivoDb = findById(id);
        if(cultivoDb == null)
            return null;
        cultivoRepository.delete(cultivoDb);
        return cultivoDb;
    }
}
