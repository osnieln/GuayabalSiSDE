package cu.edu.unah.GuayabalSiSDE.util;

import cu.edu.unah.GuayabalSiSDE.entity.MovimientoAgroquimico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoAgroquimicoResponse {

    private Long id;
    private Long agroquimicoId;
    private String agroquimicoNombre;
    private String tipo;
    private Double cantidad;
    private String fecha;
    private Long trabajadorId;
    private String trabajadorNombre;
    private String observaciones;

    public static MovimientoAgroquimicoResponse map(MovimientoAgroquimico movimientoAgroquimico) {
        if (movimientoAgroquimico == null) return null;
        return MovimientoAgroquimicoResponse.builder()
                .id(movimientoAgroquimico.getId())
                .agroquimicoId(movimientoAgroquimico.getAgroquimico() != null ? movimientoAgroquimico.getAgroquimico().getId() : null)
                .agroquimicoNombre(movimientoAgroquimico.getAgroquimico() != null ? movimientoAgroquimico.getAgroquimico().getNombre() : null)
                .tipo(movimientoAgroquimico.getTipo() != null ? movimientoAgroquimico.getTipo().name() : null)
                .cantidad(movimientoAgroquimico.getCantidad())
                .fecha(DateFormatter.format(movimientoAgroquimico.getFecha()))
                .trabajadorId(movimientoAgroquimico.getTrabajador() != null ? movimientoAgroquimico.getTrabajador().getId() : null)
                .trabajadorNombre(movimientoAgroquimico.getTrabajador() != null ? movimientoAgroquimico.getTrabajador().getNombre() : null)
                .observaciones(movimientoAgroquimico.getObservaciones())
                .build();
    }
}
