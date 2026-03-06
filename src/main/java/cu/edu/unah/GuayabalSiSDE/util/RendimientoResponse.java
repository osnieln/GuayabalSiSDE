package cu.edu.unah.GuayabalSiSDE.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RendimientoResponse {

    AreaCultivoResponsePK areaCultivoResponsePK;
    Long planProd;
    Double produccionReal;
    Double rendimientoPorcentaje;
}
