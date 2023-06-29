package boot.spring.backend.quotes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@ControllerAdvice
public class QuoteExceptionHandler {
    private static final String ERRORS_KEY = "errors";
    private static final String EMPTY_BODY_MESSAGE = "Request body cannot be null or empty.";
    public static final String GENERAL_EXCEPTION_MESSAGE = "An Error occurred";
    public static final String TYPE_MISMATCH_EXCEPTION_MESSAGE = "Type miss matched";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(getErrorsMap(errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, List<String>>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(getErrorsMap(Collections.singletonList(EMPTY_BODY_MESSAGE)));
    }

    @ExceptionHandler(QuoteNotFoundException.class)
    public ResponseEntity<Map<String, List<String>>> handleQuoteNotFoundException(QuoteNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorsMap(Collections.singletonList(ex.getMessage())));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorsMap(TYPE_MISMATCH_EXCEPTION_MESSAGE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorsMap(GENERAL_EXCEPTION_MESSAGE));
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put(ERRORS_KEY, errors);
        return errorResponse;
    }

    private Map<String, String> getErrorsMap(String error) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put(ERRORS_KEY, error);
        return errorMap;
    }
}
