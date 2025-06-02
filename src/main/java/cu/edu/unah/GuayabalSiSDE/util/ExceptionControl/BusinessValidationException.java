package cu.edu.unah.GuayabalSiSDE.util.ExceptionControl;

import lombok.Getter;

@Getter
public class BusinessValidationException extends RuntimeException {

    public BusinessValidationException(ErrorCodes errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    private final ErrorCodes errorCode;
}
