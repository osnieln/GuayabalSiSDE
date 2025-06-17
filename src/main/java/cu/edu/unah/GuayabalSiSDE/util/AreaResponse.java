package cu.edu.unah.GuayabalSiSDE.util;

import com.vividsolutions.jts.geom.Point;
import cu.edu.unah.GuayabalSiSDE.entity.Area;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AreaResponse {

    Long id;

    String descripcion;

    String ubicacion;

    String capa;

    public static Area AreaResponseToArea(AreaResponse areaResponse){
        return Area.builder()
                .id(areaResponse.getId())
                .descripcion(areaResponse.getDescripcion())
                .ubicacion((Polygon) GeometryUtil.wktToGeometry(areaResponse.ubicacion))
                .capa(areaResponse.capa)
                .build();
    }

    public static AreaResponse AreaToAreaResponse(Area area){
        return new AreaResponse(area.getId(), area.getDescripcion(), GeometryUtil.geometryToWkt(area.getUbicacion()), area.getCapa());
    }
}
