package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Agroquimico;

import java.util.List;

public interface AgroquimicoService {

    List<Agroquimico> findAll();

    Agroquimico findById(long id);

    Agroquimico findByNombre(String nombre);

    Agroquimico create(Agroquimico agroquimico);

    Agroquimico edit(Agroquimico agroquimico);

    Agroquimico delete(long id);
}
