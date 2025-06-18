package cu.edu.unah.GuayabalSiSDE.util;

import cu.edu.unah.GuayabalSiSDE.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AreaCultivoResponseReport implements Serializable {

    String area;
    String cultivo;
    String fechaSiembra;
    String fechaRecogida;
    Long planProd;
    Double prodCultivosPermanente;
    Double prodCultivosTemporales;
    Double produccionReal;
    List<String> agroquimicos;

    public static AreaCultivoResponseReport map(AreaCultivo areaCultivo){
        return AreaCultivoResponseReport.builder()
                .area(areaCultivo.getArea().getDescripcion())
                .cultivo(areaCultivo.getCultivo().getDescripcion())
                .fechaSiembra(DateFormatter.format(areaCultivo.getAreaCultivoPk().getFechaSiembra()))
                .fechaRecogida(DateFormatter.format(areaCultivo.getFechaRecogida()))
                .planProd(areaCultivo.getPlanProd())
                .prodCultivosPermanente(areaCultivo.getProdCultivosPermanente())
                .prodCultivosTemporales(areaCultivo.getProdCultivosTemporales())
                .produccionReal(areaCultivo.getProduccionReal())
                .agroquimicos(areaCultivo.getAgroquimicos().stream().map(Agroquimico::getNombre).collect(Collectors.toList()))
                .build();
    }

}
