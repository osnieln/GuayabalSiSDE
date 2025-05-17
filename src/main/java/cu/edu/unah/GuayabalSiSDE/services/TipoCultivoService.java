package cu.edu.unah.GuayabalSiSDE.services;


import cu.edu.unah.GuayabalSiSDE.entity.Produccion;
import cu.edu.unah.GuayabalSiSDE.entity.TipoCultivo;

import java.util.List;

public interface TipoCultivoService {

    List<TipoCultivo> findAll();

    TipoCultivo findById(Long id);

    TipoCultivo findByNombre(String nombre);

    TipoCultivo create(TipoCultivo tipoCultivo);

    TipoCultivo edit(TipoCultivo tipoCultivo);

    TipoCultivo delete(Long id);
}
