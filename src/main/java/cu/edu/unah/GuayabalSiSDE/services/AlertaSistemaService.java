package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.AlertaSistema;

import java.util.List;

public interface AlertaSistemaService {

    List<AlertaSistema> findAll(boolean soloNoLeidas);

    long countActivas();

    AlertaSistema marcarLeida(Long id);

    AlertaSistema marcarResuelta(Long id);

    void generarAlertas();
}
