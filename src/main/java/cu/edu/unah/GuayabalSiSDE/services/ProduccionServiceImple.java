package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Area;
import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.repository.ProduccionRepository;
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
    public Produccion create(@NonNull Produccion produccion) { return produccionRepository.save(produccion); }

    @Override
    public Produccion edit(@NonNull Produccion produccion) {
        Produccion produccionDb = findById(produccion.getId());
        if(null == produccionDb){
            return null;
        }
        produccionDb.setDescripcion(produccion.getDescripcion());
        return produccionRepository.save(produccionDb);
    }

    @Override
    public Produccion delete(Long id) {
        Produccion produccionDb = findById(id);
        if(produccionDb == null)
            return null;
        produccionRepository.delete(produccionDb);
        return produccionDb;
    }
}
