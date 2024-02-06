package boot.spring.backend.quotes.exception;

public class QuoteInternalException extends RuntimeException {

  public QuoteInternalException() {
    super();
  }

  public QuoteInternalException(String message) {
    super(message);
  }
}
