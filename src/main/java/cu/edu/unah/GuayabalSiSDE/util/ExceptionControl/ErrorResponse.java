package cu.edu.unah.GuayabalSiSDE.util.ExceptionControl;

import java.time.Instant;

public record ErrorResponse(
        String errorCode,
        String errorDescription,
        Instant timestamp,
        String path
) {
}
