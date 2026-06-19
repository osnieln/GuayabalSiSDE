package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Trabajador;

import java.util.List;

public interface TrabajadorService {

    List<Trabajador> findAll();

    Trabajador findById(Long id);

    Trabajador create(Trabajador trabajador);

    Trabajador edit(Trabajador trabajador);

    Trabajador delete(Long id);
}
