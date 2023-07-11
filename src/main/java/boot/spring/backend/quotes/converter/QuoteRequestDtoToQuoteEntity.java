package boot.spring.backend.quotes.converter;

import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.model.QuoteEntity;

/**
 * @author George Lykoudis
 * @date 7/11/2023
 */
public class QuoteRequestDtoToQuoteEntity {

    public static QuoteEntity convertFrom(QuoteRequestDto request) {
        QuoteEntity quote = new QuoteEntity();
        quote.setAuthor(request.getAuthor());
        quote.setText(request.getText());
        return quote;
    }
}
