package cu.edu.unah.GuayabalSiSDE.services;

import cu.edu.unah.GuayabalSiSDE.entity.Agroquimico;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.repository.AgroquimicoRepository;
import cu.edu.unah.GuayabalSiSDE.repository.AreaCultivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgroquimicoServiceImpl implements AgroquimicoService {

    @Autowired
    private AgroquimicoRepository agroquimicoRepository;

    @Autowired
    private AreaCultivoRepository areaCultivoRepository;

    @Override
    @Transactional
    public List<Agroquimico> findAll() {
        return agroquimicoRepository.findAll();
    }

    @Override
    @Transactional
    public Agroquimico findById(long id) {
        return agroquimicoRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Agroquimico create(Agroquimico agroquimico) {
        // Verificar si ya existe un agroquímico con el mismo nombre
        Agroquimico agroquimicoDb = agroquimicoRepository.findByNombre(agroquimico.getNombre());
        if (agroquimicoDb != null) {
            return null;
        }

        // Guardar el agroquímico primero para obtener el ID generado
        Agroquimico savedAgroquimico = agroquimicoRepository.save(agroquimico);

        // Si hay áreas de cultivo asociadas, establecer la relación bidireccional
        if (agroquimico.getAreaCultivos() != null && !agroquimico.getAreaCultivos().isEmpty()) {
            for (AreaCultivo areaCultivo : agroquimico.getAreaCultivos()) {
                // Asegurarse de que la lista de agroquímicos en AreaCultivo esté inicializada
                if (areaCultivo.getAgroquimicos() == null) {
                    areaCultivo.setAgroquimicos(new ArrayList<>());
                }
                // Agregar el agroquímico a la lista de agroquímicos del área de cultivo
                if (!areaCultivo.getAgroquimicos().contains(savedAgroquimico)) {
                    areaCultivo.getAgroquimicos().add(savedAgroquimico);
                }
            }
            // Guardar las áreas de cultivo actualizadas
            // Necesitarás inyectar el repositorio de AreaCultivo
            areaCultivoRepository.saveAll(agroquimico.getAreaCultivos());
        }

        return savedAgroquimico;
    }

    @Override
    @Transactional
    public Agroquimico edit(Agroquimico agroquimico) {
        Agroquimico agroquimicoDb = agroquimicoRepository.findByNombre(agroquimico.getNombre());
        if(agroquimicoDb == null) return null;

        // Obtener las áreas de cultivo actuales
        List<AreaCultivo> areasActuales = new ArrayList<>(agroquimicoDb.getAreaCultivos());

        // Limpiar las relaciones actuales
        for (AreaCultivo areaActual : areasActuales) {
            areaActual.getAgroquimicos().remove(agroquimicoDb);
        }
        areaCultivoRepository.saveAll(areasActuales);
        agroquimicoDb.getAreaCultivos().clear();

        // Establecer las nuevas relaciones
        if (agroquimico.getAreaCultivos() != null) {
            for (AreaCultivo nuevaArea : agroquimico.getAreaCultivos()) {
                AreaCultivo areaCultivo = areaCultivoRepository.findById(nuevaArea.getAreaCultivoPk())
                        .orElseThrow(() -> new RuntimeException("Área de cultivo no encontrada"));

                if (areaCultivo.getAgroquimicos() == null) {
                    areaCultivo.setAgroquimicos(new ArrayList<>());
                }

                if (!areaCultivo.getAgroquimicos().contains(agroquimicoDb)) {
                    areaCultivo.getAgroquimicos().add(agroquimicoDb);
                }

                if (!agroquimicoDb.getAreaCultivos().contains(areaCultivo)) {
                    agroquimicoDb.getAreaCultivos().add(areaCultivo);
                }
            }
            areaCultivoRepository.saveAll(agroquimico.getAreaCultivos());
        }

        return agroquimicoRepository.save(agroquimicoDb);
    }

    @Override
    @Transactional
    public Agroquimico delete(long id) {
        Agroquimico agroquimicoDb = findById(id);
        if (agroquimicoDb == null)
            return null;
        if(agroquimicoDb.getAreaCultivos() != null && !agroquimicoDb.getAreaCultivos().isEmpty()) {
            return null;
        }
        agroquimicoRepository.delete(agroquimicoDb);
        return agroquimicoDb;
    }
}
