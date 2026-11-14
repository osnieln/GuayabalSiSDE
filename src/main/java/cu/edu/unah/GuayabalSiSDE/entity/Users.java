package cu.edu.unah.GuayabalSiSDE.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(min = 3, max = 45, message = "El nombre de usuario debe tener entre 3 y 45 caracteres.")
    @Pattern(regexp = "[a-z]+[0-9]*[_.\\-]?[a-z0-9]+$", message = "El usuario solo puede contener minúsculas, números y los caracteres _ . -")
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotBlank(message = "La identificación es obligatoria.")
    @Size(max = 11, message = "La identificación no puede superar los 11 caracteres.")
    @Column(name = "identificacion")
    private String identificacion;
    @Basic(optional = false)
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 250, message = "El nombre debe tener entre 2 y 250 caracteres.")
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El correo electrónico no tiene un formato válido.")
    @Size(max = 250, message = "El correo no puede superar los 250 caracteres.")
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres.")
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "descripcion")
    private String descripcion;
    //@OneToMany(cascade = CascadeType.ALL, mappedBy = "users")
    //private List<Authorities> authoritiesList;
}
