package boot.spring.backend.quotes.controller;

import boot.spring.backend.quotes.exception.QuoteNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class QuoteControllerAdvise {

  private static final Logger LOGGER = LoggerFactory.getLogger(QuoteControllerAdvise.class);

  @ExceptionHandler(QuoteNotFoundException.class)
  public ResponseEntity<Void> handleQuoteNotFoundException(QuoteNotFoundException e) {
    LOGGER.info("Handle QuoteNotFoundException exception with message: {}", e.getMessage());
    return ResponseEntity.notFound().build();
  }

}
