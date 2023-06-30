package boot.spring.backend.quotes.service;

import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.model.QuoteEntity;

import java.util.List;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
public interface QuoteService {
    QuoteEntity saveQuote(QuoteRequestDto quoteRequestDto);

    QuoteEntity findQuoteById(Long id);

    QuoteEntity updateQuoteById(Long id, QuoteRequestDto quoteRequestDto);

    void deleteById(Long id);

    List<QuoteEntity> findAll();

    List<QuoteResponseDto> findAllDtos();
}
