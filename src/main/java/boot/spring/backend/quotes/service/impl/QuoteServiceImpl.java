package boot.spring.backend.quotes.service.impl;

import boot.spring.backend.quotes.converter.QuoteEntityToQuoteResponseDto;
import boot.spring.backend.quotes.converter.QuoteRequestDtoToQuoteEntity;
import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.dto.QuoteResponsePaginationDto;
import boot.spring.backend.quotes.exception.QuoteAlreadyExistException;
import boot.spring.backend.quotes.exception.QuoteNotFoundException;
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
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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
    public QuoteResponseDto saveQuote(QuoteRequestDto request) {
        Optional<QuoteEntity> existingQuote = quoteRepository.findQuoteByText(request.getText());
        if (existingQuote.isPresent()) {
            throw new QuoteAlreadyExistException();
        }

        QuoteEntity quoteToBeSaved = QuoteRequestDtoToQuoteEntity.convertFrom(request);
        QuoteEntity savedQuote = quoteRepository.save(quoteToBeSaved);
        LOG.info("Saved new quote with id {}", savedQuote.getId());
        return QuoteEntityToQuoteResponseDto.convertFrom(savedQuote);
    }

    @Override
    @Cacheable(value = Constants.QUOTES_CACHE_DB, key = "#id")
    public QuoteResponseDto findQuoteById(Long id) {
        LOG.info("Find quote by id {}", id);
        QuoteEntity quote = quoteCacheService.getQuoteById(id);
        return QuoteEntityToQuoteResponseDto.convertFrom(quote);
    }

    @Override
    @Transactional
    @CacheEvict(value = Constants.QUOTES_CACHE_DB, allEntries = true)
    @CachePut(value = Constants.QUOTES_CACHE_DB)
    public QuoteResponseDto updateQuoteById(Long id, QuoteRequestDto quoteRequestDto) {
        QuoteEntity quote = quoteCacheService.getQuoteById(id);
        LOG.info("Update quote with id {}", id);
        quote.setAuthor(quoteRequestDto.getAuthor());
        quote.setText(quoteRequestDto.getText());
        QuoteEntity savedQuote = quoteRepository.save(quote);
        return QuoteEntityToQuoteResponseDto.convertFrom(savedQuote);
    }

    @Override
    @CacheEvict(value = Constants.QUOTES_CACHE_DB, allEntries = true)
    public void deleteById(Long id) {
        if (!quoteRepository.existsById(id)) {
            throw new QuoteNotFoundException();
        }
        LOG.info("Delete quote with id {}", id);
        quoteRepository.deleteById(id);
    }

    @Override
    public List<QuoteResponseDto> findAll() {
        List<QuoteEntity> quotes = quoteCacheService.findAll();
        return convertToQuoteResponseDtos(quotes);
    }

    @Override
    public QuoteResponsePaginationDto findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<QuoteEntity> quotePage = quoteCacheService.findAll(pageable);
        QuoteResponsePaginationDto response = new QuoteResponsePaginationDto();
        response.setPage(page);
        response.setPageSize(pageSize);
        response.setTotalQuotes(quotePage.getTotalElements());
        List<QuoteEntity> content = quotePage.getContent();
        response.setQuotes(convertToQuoteResponseDtos(content));
        return response;
    }

    private List<QuoteResponseDto> convertToQuoteResponseDtos(List<QuoteEntity> quotes) {
        return quotes.stream()
                .map(QuoteEntityToQuoteResponseDto::convertFrom)
                .collect(Collectors.toList());
    }
    
    @Override
    public QuoteResponseDto findRandomQuote() {
        List<Long> quoteIds = quoteCacheService.getLimitedQuoteIds();
        if (quoteIds.isEmpty()) {
            return new QuoteResponseDto();
        }
        int randomNumber = getRandomNumber(quoteIds.size());
        LOG.info("Random Quote:");
        QuoteEntity quote = quoteCacheService.getQuoteById(quoteIds.get(randomNumber));
        return QuoteEntityToQuoteResponseDto.convertFrom(quote);
    }

    private int getRandomNumber(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    @Override
    public List<QuoteResponseDto> findQuotesHavingText(String text) {
        List<QuoteEntity> quotes = quoteCacheService.findQuotesHavingText(text);
        return convertToQuoteResponseDtos(quotes);
    }

    @Override
    public QuoteResponsePaginationDto findQuotesHavingText(String text, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<QuoteEntity> quotes = quoteCacheService.findQuotesHavingText(text, pageable);

        QuoteResponsePaginationDto response = new QuoteResponsePaginationDto();
        response.setPage(page);
        response.setPageSize(pageSize);
        response.setTotalQuotes(quotes.getTotalElements());
        List<QuoteEntity> content = quotes.getContent();
        response.setQuotes(convertToQuoteResponseDtos(content));
        return response;
    }
}
