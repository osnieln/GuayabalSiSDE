package cu.edu.unah.GuayabalSiSDE.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cultivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "La descripción del cultivo es obligatoria.")
    @Size(min = 2, max = 250, message = "La descripción debe tener entre 2 y 250 caracteres.")
    String descripcion;

    @NotNull(message = "Debe asociar una producción al cultivo.")
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "produccionid")
    Produccion produccion;

    @NotNull(message = "Debe asociar un tipo de cultivo al cultivo.")
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "tipoCultivoid")
    TipoCultivo tipoCultivo;
}
