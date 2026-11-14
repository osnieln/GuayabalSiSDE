package cu.edu.unah.GuayabalSiSDE.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanificacionCosecha implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @NonNull
    private AreaCultivoPk areaCultivoPk;

    @Enumerated(EnumType.STRING)
    private EstadoCosecha estado;

    private Date fechaEstimada;

    private LocalDateTime fechaCompletado;

    private Double cumplimientoPct;

    @Column(length = 500)
    private String observaciones;
}
