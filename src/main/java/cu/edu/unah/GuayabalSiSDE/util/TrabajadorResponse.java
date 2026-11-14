package cu.edu.unah.GuayabalSiSDE.util;

import cu.edu.unah.GuayabalSiSDE.entity.Area;
import cu.edu.unah.GuayabalSiSDE.entity.Riego;
import cu.edu.unah.GuayabalSiSDE.entity.Trabajador;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrabajadorResponse {

    @Id
    private Long id;

    private String nombre;

    private String identificacion;

    private String cargo;

    private String telefono;

    private boolean activo;

    private List<Long> areaIds;

    private List<Long> riegoIds;

    public static TrabajadorResponse map(Trabajador trabajador) {
        if (trabajador == null) return null;
        if (trabajador.getAreas() == null) trabajador.setAreas(new ArrayList<>());
        if (trabajador.getRiegos() == null) trabajador.setRiegos(new ArrayList<>());
        List<Long> areaIds = trabajador.getAreas().stream().map(Area::getId).toList();
        List<Long> riegoIds = trabajador.getRiegos().stream().map(Riego::getId).toList();
        return TrabajadorResponse.builder()
                .id(trabajador.getId())
                .nombre(trabajador.getNombre())
                .identificacion(trabajador.getIdentificacion())
                .cargo(trabajador.getCargo())
                .telefono(trabajador.getTelefono())
                .activo(trabajador.isActivo())
                .areaIds(areaIds)
                .riegoIds(riegoIds)
                .build();
    }

    public static Trabajador map(TrabajadorResponse trabajadorResponse) {
        if (trabajadorResponse == null) return null;
        if (trabajadorResponse.getAreaIds() == null) trabajadorResponse.setAreaIds(new ArrayList<>());
        if (trabajadorResponse.getRiegoIds() == null) trabajadorResponse.setRiegoIds(new ArrayList<>());
        List<Area> areas = new ArrayList<>();
        trabajadorResponse.getAreaIds().forEach(areaId -> areas.add(Area.builder().id(areaId).build()));
        List<Riego> riegos = new ArrayList<>();
        trabajadorResponse.getRiegoIds().forEach(riegoId -> riegos.add(Riego.builder().id(riegoId).build()));
        return Trabajador.builder()
                .id(trabajadorResponse.getId())
                .nombre(trabajadorResponse.getNombre())
                .identificacion(trabajadorResponse.getIdentificacion())
                .cargo(trabajadorResponse.getCargo())
                .telefono(trabajadorResponse.getTelefono())
                .activo(trabajadorResponse.isActivo())
                .areas(areas)
                .riegos(riegos)
                .build();
    }
}
