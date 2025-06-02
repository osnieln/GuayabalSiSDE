package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Riego;
import cu.edu.unah.GuayabalSiSDE.repository.RiegoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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
    public Riego create(Riego riego) {
        AreaCultivo areaCultivo = areaCultivoService.findById(riego.getAreaCultivo().getAreaCultivoPk());
        if(areaCultivo == null) {
            return null;
        }
        riego.setAreaCultivo(areaCultivo);
        return riegoRepository.save(riego);
    }

    @Override
    public Riego edit(Riego riego) {
        Riego riegoDb = findById(riego.getId());
        if(riegoDb == null) {
            return null;
        }
        AreaCultivo areaCultivoDb = areaCultivoService.findById(riego.getAreaCultivo().getAreaCultivoPk());
        if(areaCultivoDb == null) {
            return null;
        }
        riegoDb.setFechaReal(riego.getFechaReal());
        riegoDb.setFechaPlanificacion(riego.getFechaPlanificacion());
        riegoDb.setAreaCultivo(areaCultivoDb);
        return riegoRepository.save(riegoDb);
    }

    @Override
    public Riego delete(long id) {
        Riego riegoDb = findById(id);
        if(riegoDb == null) {
            return null;
        }
        riegoRepository.delete(riegoDb);
        return riegoDb;
    }
}
