package cu.edu.unah.GuayabalSiSDE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthoritiesPK implements Serializable {

	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "authority")
    private String authority;
}
