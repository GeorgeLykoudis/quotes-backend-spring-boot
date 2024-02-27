package boot.spring.backend.quotes.service.quotes;

import boot.spring.backend.quotes.dto.quotes.QuoteRequestDto;
import boot.spring.backend.quotes.dto.quotes.QuoteResponseDto;
import boot.spring.backend.quotes.dto.quotes.QuoteResponsePaginationDto;
import boot.spring.backend.quotes.exception.QuoteAlreadyExistException;
import boot.spring.backend.quotes.exception.QuoteNotFoundException;

import java.security.Principal;
import java.util.List;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
public interface QuoteService {
    QuoteResponseDto saveQuote(QuoteRequestDto quoteRequestDto) throws QuoteAlreadyExistException;

    QuoteResponseDto findQuoteById(Long id) throws QuoteNotFoundException, IllegalArgumentException;

    QuoteResponseDto updateQuote(QuoteRequestDto quoteRequestDto) throws QuoteNotFoundException, IllegalArgumentException;

    void deleteById(Long id) throws QuoteNotFoundException, IllegalArgumentException;

    List<QuoteResponseDto> findAll() throws IllegalArgumentException;

    QuoteResponsePaginationDto findAll(int page, int pageSize) throws IllegalArgumentException;

    QuoteResponseDto findRandomQuote();

    List<QuoteResponseDto> findQuotesHavingText(String text);

    QuoteResponsePaginationDto findQuotesHavingText(String text, int page, int pageSize);
}
