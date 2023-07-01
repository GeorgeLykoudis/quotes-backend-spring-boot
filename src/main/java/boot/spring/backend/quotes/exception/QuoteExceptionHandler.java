package boot.spring.backend.quotes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static boot.spring.backend.quotes.exception.ErrorConstants.EMPTY_BODY_MESSAGE;
import static boot.spring.backend.quotes.exception.ErrorConstants.ERRORS_KEY;
import static boot.spring.backend.quotes.exception.ErrorConstants.GENERAL_EXCEPTION_MESSAGE;
import static boot.spring.backend.quotes.exception.ErrorConstants.TYPE_MISMATCH_EXCEPTION_MESSAGE;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@ControllerAdvice
public class QuoteExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(getErrorsMap(errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(getErrorsMap(EMPTY_BODY_MESSAGE));
    }

    @ExceptionHandler(QuoteNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleQuoteNotFoundException(QuoteNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorsMap(ex.getMessage()));
    }

    @ExceptionHandler(QuoteTableEmptyException.class)
    public ResponseEntity<Map<String, String>> handleQuoteTableEmptyException(QuoteTableEmptyException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorsMap(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorsMap(TYPE_MISMATCH_EXCEPTION_MESSAGE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getErrorsMap(GENERAL_EXCEPTION_MESSAGE));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorsMap(ex.getMessage()));
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
