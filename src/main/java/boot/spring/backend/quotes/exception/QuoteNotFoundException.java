package boot.spring.backend.quotes.exception;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
public class QuoteNotFoundException extends RuntimeException {
    private static final String QUOTE_NOT_FOUND = "Could not find quote id ";

    public QuoteNotFoundException(Long id) {
        super(QUOTE_NOT_FOUND + id);
    }
}
