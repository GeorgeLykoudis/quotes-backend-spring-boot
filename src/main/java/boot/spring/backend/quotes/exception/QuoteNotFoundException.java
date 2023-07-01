package boot.spring.backend.quotes.exception;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
public class QuoteNotFoundException extends RuntimeException {

    public QuoteNotFoundException(String message) {
        super(message);
    }
}
