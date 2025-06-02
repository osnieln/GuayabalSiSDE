package cu.edu.unah.GuayabalSiSDE.util.ExceptionControl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCodes {
    MISSING_REQUIRED_FIELD("001", "Required field not provided."),
    INVALID_PARAMETER_COMBINATION("002", "Invalid parameter combination."),
    INVALID_DATE_FORMAT("003", "Invalid date format."),
    OPERATION_VALIDATION_ERROR("004", "Validation error in operation.");

    private final String code;
    private final String description;
}
