package cu.edu.unah.GuayabalSiSDE.util;

import cu.edu.unah.GuayabalSiSDE.entity.AreaCultivo;
import cu.edu.unah.GuayabalSiSDE.entity.PlanificacionCosecha;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanificacionCosechaResponse {

    AreaCultivoResponsePK areaCultivoResponsePk;
    String areaDescripcion;
    String cultivoDescripcion;
    String fechaEstimada;
    String estado;
    Long planProd;
    Double produccionReal;
    Double cumplimientoPct;
    String fechaCompletado;

    public static PlanificacionCosechaResponse map(PlanificacionCosecha pc, AreaCultivo ac) {
        return PlanificacionCosechaResponse.builder()
                .areaCultivoResponsePk(AreaCultivoResponsePK.map(ac.getAreaCultivoPk()))
                .areaDescripcion(ac.getArea() != null ? ac.getArea().getDescripcion() : null)
                .cultivoDescripcion(ac.getCultivo() != null ? ac.getCultivo().getDescripcion() : null)
                .fechaEstimada(pc.getFechaEstimada() != null ? DateFormatter.format(pc.getFechaEstimada()) : null)
                .estado(pc.getEstado() != null ? pc.getEstado().name() : null)
                .planProd(ac.getPlanProd())
                .produccionReal(ac.getProduccionReal())
                .cumplimientoPct(pc.getCumplimientoPct())
                .fechaCompletado(pc.getFechaCompletado() != null ? pc.getFechaCompletado().toString() : null)
                .build();
    }
}
