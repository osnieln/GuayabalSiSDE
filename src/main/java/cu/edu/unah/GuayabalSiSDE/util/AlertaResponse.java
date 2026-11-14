package cu.edu.unah.GuayabalSiSDE.util;

import cu.edu.unah.GuayabalSiSDE.entity.AlertaSistema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlertaResponse {

    Long id;
    String tipo;
    String prioridad;
    String mensaje;
    String fechaReferencia;
    AreaCultivoResponsePK areaCultivoResponsePk;
    Long agroquimicoId;
    String fechaGeneracion;
    boolean leida;
    boolean resuelta;

    public static AlertaResponse map(AlertaSistema a) {
        AreaCultivoResponsePK pk = null;
        if (a.getAreaId() != null && a.getCultivoId() != null && a.getFechaSiembra() != null) {
            pk = AreaCultivoResponsePK.builder()
                    .areaId(a.getAreaId())
                    .cultivoId(a.getCultivoId())
                    .fechaSiembra(DateFormatter.format(a.getFechaSiembra()))
                    .build();
        }

        return AlertaResponse.builder()
                .id(a.getId())
                .tipo(a.getTipo() != null ? a.getTipo().name() : null)
                .prioridad(a.getPrioridad() != null ? a.getPrioridad().name() : null)
                .mensaje(a.getMensaje())
                .fechaReferencia(a.getFechaReferencia() != null ? DateFormatter.format(a.getFechaReferencia()) : null)
                .areaCultivoResponsePk(pk)
                .agroquimicoId(a.getAgroquimicoId())
                .fechaGeneracion(a.getFechaGeneracion() != null ? a.getFechaGeneracion().toString() : null)
                .leida(a.isLeida())
                .resuelta(a.isResuelta())
                .build();
    }
}
