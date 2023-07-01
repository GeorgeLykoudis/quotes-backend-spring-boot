package boot.spring.backend.quotes.exception;

/**
 * @author George Lykoudis
 * @date 7/1/2023
 */
public class QuoteTableEmptyException extends RuntimeException {

    public QuoteTableEmptyException(String message) {
        super(message);
    }
}
