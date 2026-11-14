package cu.edu.unah.GuayabalSiSDE.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trabajador implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del trabajador es obligatorio.")
    @Size(min = 2, max = 250, message = "El nombre debe tener entre 2 y 250 caracteres.")
    private String nombre;

    @NotBlank(message = "La identificación del trabajador es obligatoria.")
    @Column(unique = true)
    private String identificacion;

    private String cargo;

    private String telefono;

    private boolean activo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "trabajador_area",
            joinColumns = @JoinColumn(name = "trabajador_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id")
    )
    private List<Area> areas;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "trabajador_riego",
            joinColumns = @JoinColumn(name = "trabajador_id"),
            inverseJoinColumns = @JoinColumn(name = "riego_id")
    )
    private List<Riego> riegos;
}
