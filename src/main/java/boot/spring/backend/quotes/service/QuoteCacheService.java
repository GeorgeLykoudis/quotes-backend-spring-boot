package boot.spring.backend.quotes.service;

import boot.spring.backend.quotes.model.QuoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author George Lykoudis
 * @date 7/1/2023
 */
public interface QuoteCacheService {
    QuoteEntity getQuoteById(Long id);

    List<Long> getLimitedQuoteIds();

    List<QuoteEntity> findAll();

    Page<QuoteEntity> findAll(int page, int quotesPerPage);

    List<QuoteEntity> findQuotesHavingText(String text);

    List<QuoteEntity> findQuotesHavingText(String text, Pageable pageable);
}
