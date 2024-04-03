package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Produccion;

import java.util.List;

public interface ProduccionService {

    List<Produccion> findAll();

    Produccion findById(Long id);

    Produccion create(Produccion produccion);

    Produccion edit(Produccion produccion);


    Produccion delete(Long id);

}

