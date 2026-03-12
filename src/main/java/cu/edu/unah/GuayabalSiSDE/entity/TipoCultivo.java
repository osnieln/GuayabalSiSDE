package cu.edu.unah.GuayabalSiSDE.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class TipoCultivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "El nombre del tipo de cultivo es obligatorio.")
    @Size(min = 2, max = 250, message = "El nombre debe tener entre 2 y 250 caracteres.")
    String nombre;
}
