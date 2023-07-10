package boot.spring.backend.quotes.exception;

import boot.spring.backend.quotes.dto.QuoteErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@ControllerAdvice
public class QuoteExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<QuoteErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
//        List<String> errors = ex.getBindingResult().getFieldErrors()
//                .stream()
//                .map(FieldError::getDefaultMessage)
//                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(QuoteErrorResponseDto.of(ErrorConstants.TYPE_MISMATCH_EXCEPTION_MESSAGE.getCode()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<QuoteErrorResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(QuoteErrorResponseDto.of(ErrorConstants.EMPTY_BODY_MESSAGE.getCode()));
    }

    @ExceptionHandler(QuoteNotFoundException.class)
    public ResponseEntity<QuoteErrorResponseDto> handleQuoteNotFoundException(QuoteNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(QuoteErrorResponseDto.of(ErrorConstants.QUOTE_NOT_FOUND.getCode()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<QuoteErrorResponseDto> handleArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(QuoteErrorResponseDto.of(ErrorConstants.TYPE_MISMATCH_EXCEPTION_MESSAGE.getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<QuoteErrorResponseDto> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(QuoteErrorResponseDto.of(ErrorConstants.GENERAL_EXCEPTION_MESSAGE.getCode()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<QuoteErrorResponseDto> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(QuoteErrorResponseDto.of(ErrorConstants.MISSING_REQUEST_PARAMETER.getCode()));
    }

    @ExceptionHandler(QuoteAlreadyExistException.class)
    public ResponseEntity<QuoteErrorResponseDto> handleQuoteAlreadyExistException(QuoteAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(QuoteErrorResponseDto.of(ErrorConstants.QUOTE_ALREADY_EXIST.getCode()));
    }
}
