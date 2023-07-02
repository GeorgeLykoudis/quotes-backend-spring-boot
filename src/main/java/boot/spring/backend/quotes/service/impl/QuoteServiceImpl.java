package boot.spring.backend.quotes.service.impl;

import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.exception.QuoteNotFoundException;
import boot.spring.backend.quotes.exception.QuoteTableEmptyException;
import boot.spring.backend.quotes.model.QuoteEntity;
import boot.spring.backend.quotes.repository.QuoteRepository;
import boot.spring.backend.quotes.service.QuoteCacheService;
import boot.spring.backend.quotes.service.QuoteService;
import boot.spring.backend.quotes.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static boot.spring.backend.quotes.exception.ErrorConstants.EMPTY_TABLE;
import static boot.spring.backend.quotes.exception.ErrorConstants.QUOTE_NOT_FOUND;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@Service
public class QuoteServiceImpl implements QuoteService {
    private static final Logger LOG = LoggerFactory.getLogger(QuoteServiceImpl.class);
    private final QuoteRepository quoteRepository;
    private final QuoteCacheService quoteCacheService;

    public QuoteServiceImpl(QuoteRepository quoteRepository, QuoteCacheService quoteCacheService) {
        this.quoteRepository = quoteRepository;
        this.quoteCacheService = quoteCacheService;
    }

    @Override
    @Transactional
    @CacheEvict(value = Constants.QUOTES_CACHE_DB, allEntries = true)
    public QuoteEntity saveQuote(QuoteRequestDto quoteRequestDto) {
        QuoteEntity quoteEntity = QuoteEntity.createQuoteEntity(quoteRequestDto);
        QuoteEntity savedQuote = quoteRepository.save(quoteEntity);
        LOG.info("Saved new quote with id {}", savedQuote.getId());
        return savedQuote;
    }

    @Override
    @Cacheable(value = Constants.QUOTES_CACHE_DB, key = "#id")
    public QuoteEntity findQuoteById(Long id) {
        LOG.info("Find quote by id {}", id);
        return quoteCacheService.getQuoteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = Constants.QUOTES_CACHE_DB, allEntries = true)
    @CachePut(value = Constants.QUOTES_CACHE_DB)
    public QuoteEntity updateQuoteById(Long id, QuoteRequestDto quoteRequestDto) {
        QuoteEntity quote = quoteCacheService.getQuoteById(id);
        LOG.info("Update quote with id {}", id);
        quote.setAuthor(quoteRequestDto.getAuthor());
        quote.setText(quoteRequestDto.getText());
        return quoteRepository.save(quote);
    }

    @Override
    @CacheEvict(value = Constants.QUOTES_CACHE_DB, allEntries = true)
    public void deleteById(Long id) {
        if (!quoteRepository.existsById(id)) {
            throw new QuoteNotFoundException(QUOTE_NOT_FOUND + id);
        }
        LOG.info("Delete quote with id {}", id);
        quoteRepository.deleteById(id);
    }

    @Override
    public List<QuoteResponseDto> findAllDtos() {
        List<QuoteEntity> quotes = quoteCacheService.findAll();
        return convertToQuoteResponseDtos(quotes);
    }

    @Override
    public List<QuoteResponseDto> findAllDtos(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<QuoteEntity> quotePage = quoteCacheService.findAll(pageable);
        return convertToQuoteResponseDtos(quotePage.getContent());
    }

    private List<QuoteResponseDto> convertToQuoteResponseDtos(List<QuoteEntity> quotes) {
        return quotes.stream()
                .map(QuoteResponseDto::createQuote)
                .collect(Collectors.toList());
    }
    
    @Override
    public QuoteEntity findRandomQuote() {
        List<Long> quoteIds = quoteCacheService.getLimitedQuoteIds();
        if (quoteIds.isEmpty()) {
            throw new QuoteTableEmptyException(EMPTY_TABLE);
        }
        int randomNumber = getRandomNumber(quoteIds.size());
        LOG.info("Random Quote:");
        return quoteCacheService.getQuoteById(quoteIds.get(randomNumber));
    }

    private int getRandomNumber(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    @Override
    public List<QuoteResponseDto> findQuotesDtosHavingText(String text) {
        List<QuoteEntity> quotes = quoteCacheService.findQuotesHavingText(text);
        return convertToQuoteResponseDtos(quotes);
    }

    @Override
    public List<QuoteResponseDto> findQuotesDtosHavingText(String text, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<QuoteEntity> quotes = quoteCacheService.findQuotesHavingText(text, pageable);
        return convertToQuoteResponseDtos(quotes);
    }
}
