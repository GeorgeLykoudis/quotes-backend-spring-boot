package boot.spring.backend.quotes.exception;

import boot.spring.backend.quotes.dto.QuoteErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

import static boot.spring.backend.quotes.exception.ErrorConstants.EMPTY_BODY_MESSAGE;
import static boot.spring.backend.quotes.exception.ErrorConstants.GENERAL_EXCEPTION_MESSAGE;
import static boot.spring.backend.quotes.exception.ErrorConstants.TYPE_MISMATCH_EXCEPTION_MESSAGE;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@ControllerAdvice
public class QuoteExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<QuoteErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(QuoteErrorResponseDto.getErrorsResponse(errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<QuoteErrorResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(QuoteErrorResponseDto.getErrorResponse(EMPTY_BODY_MESSAGE));
    }

    @ExceptionHandler(QuoteNotFoundException.class)
    public ResponseEntity<QuoteErrorResponseDto> handleQuoteNotFoundException(QuoteNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(QuoteErrorResponseDto.getErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(QuoteTableEmptyException.class)
    public ResponseEntity<QuoteErrorResponseDto> handleQuoteTableEmptyException(QuoteTableEmptyException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(QuoteErrorResponseDto.getErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<QuoteErrorResponseDto> handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(QuoteErrorResponseDto.getErrorResponse(TYPE_MISMATCH_EXCEPTION_MESSAGE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<QuoteErrorResponseDto> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(QuoteErrorResponseDto.getErrorResponse(GENERAL_EXCEPTION_MESSAGE));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<QuoteErrorResponseDto> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(QuoteErrorResponseDto.getErrorResponse(ex.getMessage()));
    }
}
