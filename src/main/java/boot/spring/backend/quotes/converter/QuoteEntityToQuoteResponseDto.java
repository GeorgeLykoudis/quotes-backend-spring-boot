package boot.spring.backend.quotes.converter;

import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.model.QuoteEntity;

/**
 * @author George Lykoudis
 * @date 7/10/2023
 */
public class QuoteEntityToQuoteResponseDto {

    public static QuoteResponseDto convertFrom(QuoteEntity entity) {
        QuoteResponseDto quoteResponse = new QuoteResponseDto();
        quoteResponse.setId(entity.getId());
        quoteResponse.setAuthor(entity.getAuthor());
        quoteResponse.setText(entity.getText());
        return quoteResponse;
    }
}
