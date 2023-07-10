package boot.spring.backend.quotes.exception;

/**
 * @author George Lykoudis
 * @date 7/2/2023
 */
public class QuoteAlreadyExistException extends RuntimeException {
    public QuoteAlreadyExistException() {
        super();
    }

    public QuoteAlreadyExistException(String message) {
        super(message);
    }
}
