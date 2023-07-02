package boot.spring.backend.quotes.dto;

import boot.spring.backend.quotes.model.QuoteEntity;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
public class QuoteResponseDto extends QuoteRequestDto {
    private Long id;

    public QuoteResponseDto() {}

    private QuoteResponseDto(QuoteEntity quoteEntity) {
        super(quoteEntity);
        this.id = quoteEntity.getId();
    }

    public static QuoteResponseDto createQuote(QuoteEntity quote) {
        return new QuoteResponseDto(quote);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
