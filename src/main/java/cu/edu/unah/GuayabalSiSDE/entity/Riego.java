package cu.edu.unah.GuayabalSiSDE.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Riego implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date fechaPlanificacion;
    private Date fechaReal;

    // Relación ManyToOne con AreaCultivo (que tiene clave embebida)
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "area_cultivo_areaid", referencedColumnName = "areaId", insertable = false, updatable = false),
            @JoinColumn(name = "area_cultivo_cultivoid", referencedColumnName = "cultivoId", insertable = false, updatable = false),
            @JoinColumn(name = "area_cultivo_fecha_siembra", referencedColumnName = "fechaSiembra", insertable = false, updatable = false)
    })
    private AreaCultivo areaCultivo;
}