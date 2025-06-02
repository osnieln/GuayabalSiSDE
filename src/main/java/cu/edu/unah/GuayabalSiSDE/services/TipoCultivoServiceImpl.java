package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;
import cu.edu.unah.GuayabalSiSDE.repository.TipoCultivoRepository;
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
        TipoCultivo tipoCultivoDb = findByNombre(tipoCultivo.getNombre());
        if (tipoCultivoDb != null)
            return null;
        else
            return tipoCultivoRepository.save(tipoCultivo);
    }

    @Override
    public TipoCultivo edit(TipoCultivo tipoCultivo) {
        TipoCultivo tipoCultivoDb = findById(tipoCultivo.getId());
        if (tipoCultivoDb == null)
            return null;
        tipoCultivoDb.setNombre(tipoCultivo.getNombre());
        return tipoCultivoRepository.save(tipoCultivoDb);
    }

    @Override
    public TipoCultivo delete(Long id) {
        TipoCultivo tipoCultivoDb = findById(id);
        if (tipoCultivoDb == null)
            return null;
        tipoCultivoRepository.delete(tipoCultivoDb);
        return tipoCultivoDb;
    }
}
