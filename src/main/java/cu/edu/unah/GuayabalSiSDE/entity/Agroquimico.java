package cu.edu.unah.GuayabalSiSDE.entity;

import jakarta.persistence.*;
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

    @Column(unique = true)
    private String nombre;

    // Relación ManyToMany con AreaCultivo
    @ManyToMany(mappedBy = "agroquimicos", fetch = FetchType.LAZY)
    private List<AreaCultivo> areaCultivos;
}