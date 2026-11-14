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
public class AlertaSistema implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoAlerta tipo;

    @Enumerated(EnumType.STRING)
    private PrioridadAlerta prioridad;

    @Column(length = 500)
    private String mensaje;

    private Date fechaReferencia;

    // Clave natural para alertas ligadas a un área-cultivo (nulas para alertas de agroquímico)
    private Long areaId;
    private Long cultivoId;
    private Date fechaSiembra;

    // Clave natural para alertas ligadas a un agroquímico (nula para alertas de área-cultivo)
    private Long agroquimicoId;

    private LocalDateTime fechaGeneracion;
    private LocalDateTime fechaLectura;
    private LocalDateTime fechaResolucion;

    @Builder.Default
    private boolean leida = false;

    @Builder.Default
    private boolean resuelta = false;
}
