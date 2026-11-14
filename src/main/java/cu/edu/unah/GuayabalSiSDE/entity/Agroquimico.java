package cu.edu.unah.GuayabalSiSDE.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agroquimico implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del agroquímico es obligatorio.")
    @Size(min = 2, max = 250, message = "El nombre debe tener entre 2 y 250 caracteres.")
    @Column(unique = true)
    private String nombre;

    private Double stockActual;

    private Double stockMinimo;

    // Relación ManyToMany con AreaCultivo
    @ManyToMany(mappedBy = "agroquimicos", fetch = FetchType.LAZY)
    private List<AreaCultivo> areaCultivos;
}