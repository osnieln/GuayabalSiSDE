package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Riego;

import java.util.List;

public interface RiegoService {

    List<Riego> findAll();
    Riego findById(long id);
    Riego create(Riego riego);
    Riego edit(Riego riego);
    Riego delete(long id);
}
