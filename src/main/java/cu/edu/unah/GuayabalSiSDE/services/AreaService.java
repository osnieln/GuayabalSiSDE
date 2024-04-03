package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Area;

import java.util.List;

public interface AreaService {

    List<Area> findAll();

    Area findByID(Long id);

    Area createArea(Area area);

    Area editArea(Area area);

    Area deleteArea(Long id);
}
