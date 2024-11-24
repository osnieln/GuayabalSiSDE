
package cu.edu.unah.GuayabalSiSDE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "authorities")
public class Authorities implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AuthoritiesPK authoritiesPK;

    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;
}
