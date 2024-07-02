package cu.edu.unah.GuayabalSiSDE.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains the functions to handle exceptions during request execution. To do this, it
 * is necessary to previously create an object of type {@link BindingResult} which contains the
 * details of the exception thrown and then a String is obtained with the formatted results. <br>
 * Different exceptions can be caught that are cataloged in two fundamental groups organized by the
 * code: </br>
 *
 * <ul>
 *   <li><strong>Code 1:</strong> Exceptions thrown at the entity level. These exceptions are caught
 *       at the time of parsing the data provided in the request.
 *   <li><strong>Code 2:</strong> Exceptions thrown at execution level. These exceptions are thrown
 *       at the time of searching the database and executing business logic.
 * </ul>
 *
 * @version 2.0.0
 */
@Slf4j
public class ExceptionsBuilder {

    /**
     * This function allows you to format an exception message from a {@link BindingResult}. A
     * {@link String} is obtained with the formatted text. This function is used to format type 2
     * exceptions.
     *
     * @param result {@link BindingResult} containing the details of the exception to be thrown.
     * @return Returns a {@link String} with the details of the exception thrown.
     */
    public static String formatCustomMessage(BindingResult result) {
        List<ObjectError> errors = result.getAllErrors();
        List<Map<String, String>> errorsList = new ArrayList<>();
        errors.forEach(
                objectError -> {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(objectError.getObjectName(), objectError.getDefaultMessage());
                    errorsList.add(hashMap);
                });
        log.warn(Arrays.toString(new List[] {errorsList}));
        return Arrays.toString(new List[] {errorsList});
    }

    /**
     * @param className Name of the class that throws the exception.
     * @param exceptionMessage Exception message to be thrown.
     */
    public static void launchException(
            String className, String exceptionMessage) {
        BindingResult result1 = new BindException(new Exception(), "");
        result1.addError(new ObjectError(className, exceptionMessage));
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, ExceptionsBuilder.formatCustomMessage(result1));
    }
}
