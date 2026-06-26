package cu.edu.unah.GuayabalSiSDE.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "configuracion_correo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionCorreo {

    @Id
    private Long id;

    @Column(name = "host")
    private String host;

    @Column(name = "puerto")
    private int puerto;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "remitente")
    private String remitente;

    @Column(name = "activo")
    private boolean activo;
}
