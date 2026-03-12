package cu.edu.unah.GuayabalSiSDE.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "La fecha planificada del riego es obligatoria.")
    private Date fechaPlanificacion;
    private Date fechaReal;

    // Relación ManyToOne con AreaCultivo (que tiene clave embebida)
    @NotNull(message = "El área de cultivo del riego es obligatoria.")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "area_cultivo_areaid", referencedColumnName = "areaId"),
            @JoinColumn(name = "area_cultivo_cultivoid", referencedColumnName = "cultivoId"),
            @JoinColumn(name = "area_cultivo_fecha_siembra", referencedColumnName = "fechaSiembra")
    })
    private AreaCultivo areaCultivo;
}