package cu.edu.unah.GuayabalSiSDE.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CultivoDistribucionResponse implements Serializable {

    private String cultivo;
    private long cantidad;
}
