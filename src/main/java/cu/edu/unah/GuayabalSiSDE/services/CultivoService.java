package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Produccion;

import java.util.List;

public interface CultivoService {

    List<Cultivo> findAll();

    Cultivo findById(Long id);

    List<Cultivo> findByProduccion(Long idProduccion);

    Cultivo create(Cultivo cultivo);

    Cultivo edit(Cultivo cultivo);

    Cultivo delete(Long id);

}
