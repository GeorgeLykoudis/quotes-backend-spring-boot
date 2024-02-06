package boot.spring.backend.quotes.service.impl;

import boot.spring.backend.quotes.exception.QuoteNotFoundException;
import boot.spring.backend.quotes.model.QuoteEntity;
import boot.spring.backend.quotes.repository.QuoteRepository;
import boot.spring.backend.quotes.service.QuoteCacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static boot.spring.backend.quotes.service.cache.CacheConstants.QUOTE_CACHE;

/**
 * @author George Lykoudis
 * @date 7/1/2023
 */
@Service
public class QuoteCacheServiceImpl implements QuoteCacheService {
    private final QuoteRepository quoteRepository;

    public QuoteCacheServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
//    @Cacheable(value = Constants.QUOTES_CACHE_DB)
    public QuoteEntity getQuoteById(Long id) {
        return quoteRepository.findById(id).orElseThrow(QuoteNotFoundException::new);
    }

    @Override
    @Cacheable(value = QUOTE_CACHE)
    public List<Long> getLimitedQuoteIds() {
        return quoteRepository.findLimitedQuoteIds();
    }

    @Override
    @Cacheable(value = QUOTE_CACHE)
    public List<QuoteEntity> findAll() {
        return quoteRepository.findAll();
    }

    @Override
    @Cacheable(value = QUOTE_CACHE)
    public Page<QuoteEntity> findAll(Pageable pageable) {
        return quoteRepository.findAll(pageable);
    }

    @Override
    @Cacheable(value = QUOTE_CACHE, key = "#text")
    public List<QuoteEntity> findQuotesHavingText(String text) {
        return quoteRepository.findByTextContaining(text);
    }

    @Override
    @Cacheable(value = QUOTE_CACHE, key = "#text")
    public Page<QuoteEntity> findQuotesHavingText(String text, Pageable pageable) {
        return quoteRepository.findByTextContaining(text, pageable);
    }
}
