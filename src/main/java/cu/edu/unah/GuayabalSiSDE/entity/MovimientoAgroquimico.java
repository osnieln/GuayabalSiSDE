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
public class MovimientoAgroquimico implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El agroquímico del movimiento es obligatorio.")
    @ManyToOne(fetch = FetchType.LAZY)
    private Agroquimico agroquimico;

    @NotNull(message = "El tipo de movimiento es obligatorio.")
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;

    @NotNull(message = "La cantidad del movimiento es obligatoria.")
    private Double cantidad;

    @NotNull(message = "La fecha del movimiento es obligatoria.")
    private Date fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    private Trabajador trabajador;

    private String observaciones;
}
