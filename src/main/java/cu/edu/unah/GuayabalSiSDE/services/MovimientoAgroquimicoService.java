package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.MovimientoAgroquimico;

import java.util.List;

public interface MovimientoAgroquimicoService {

    List<MovimientoAgroquimico> findAll();

    MovimientoAgroquimico findById(Long id);

    List<MovimientoAgroquimico> findByAgroquimico(Long agroquimicoId);

    MovimientoAgroquimico create(MovimientoAgroquimico movimientoAgroquimico);

    MovimientoAgroquimico delete(Long id);
}
