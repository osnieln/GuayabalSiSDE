package cu.edu.unah.GuayabalSiSDE.util;

import cu.edu.unah.GuayabalSiSDE.entity.Area;
import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Cultivo;
import cu.edu.unah.GuayabalSiSDE.entity.Riego;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RiegoResponse {

    Long id;
    AreaCultivoResponsePK areaCultivoResponsePk;
    String fechaPlanificacion;
    String fechaReal;

    public static RiegoResponse map(Riego riego){
        return RiegoResponse.builder()
                .id(riego.getId())
                .areaCultivoResponsePk(AreaCultivoResponsePK.map(riego.getAreaCultivo().getAreaCultivoPk()))
                .fechaPlanificacion(DateFormatter.format(riego.getFechaPlanificacion()))
                .fechaReal(DateFormatter.format(riego.getFechaReal()))
                .build();
    }

    public static Riego map(RiegoResponse riegoResponse, AreaCultivo areaCultivo){
        return Riego.builder()
                .id(riegoResponse.id)
                .areaCultivo(areaCultivo)
                .fechaPlanificacion(DateFormatter.format(riegoResponse.getFechaPlanificacion()))
                .fechaReal(DateFormatter.format(riegoResponse.getFechaReal()))
                .build();
    }
}
