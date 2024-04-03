package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Area;
import cu.edu.unah.GuayabalSiSDE.repository.AreaRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService{

    @Autowired
    AreaRepository areaRepository;

    @Override
    public List<Area> findAll() {
        return areaRepository.findAll();
    }

    @Override
    public Area findByID(Long id) {
        return areaRepository.findById(id).orElse(null);
    }

    @Override
    public Area createArea(@NonNull Area area) {
        return areaRepository.save(area);
    }

    @Override
    public Area editArea(@NonNull Area area) {
        Area areaDb = findByID(area.getId());
        if(null == areaDb){
            return null;
        }
        areaDb.setDescripcion(area.getDescripcion());
        areaDb.setUbicacion(area.getUbicacion());
        return areaRepository.save(areaDb);
    }

    @Override
    public Area deleteArea(Long id) {
        Area areaDb = findByID(id);
        if(null == areaDb){
            return null;
        }
        areaRepository.delete(areaDb);
        return areaDb;
    }
}
