package cu.edu.unah.GuayabalSiSDE.util;

import cu.edu.unah.GuayabalSiSDE.entity.Agroquimico;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgroquimicoReporteResponse implements Serializable {

    private Long id;
    private String nombre;
    private int totalCultivos;

    public static AgroquimicoReporteResponse map(Agroquimico agroquimico) {
        return AgroquimicoReporteResponse.builder()
                .id(agroquimico.getId())
                .nombre(agroquimico.getNombre())
                .totalCultivos(agroquimico.getAreaCultivos() != null ? agroquimico.getAreaCultivos().size() : 0)
                .build();
    }
}
