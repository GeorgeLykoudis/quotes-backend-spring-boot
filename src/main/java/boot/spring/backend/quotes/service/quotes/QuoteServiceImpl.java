package boot.spring.backend.quotes.service.quotes;

import boot.spring.backend.quotes.dto.quotes.QuoteRequestDto;
import boot.spring.backend.quotes.dto.quotes.QuoteResponseDto;
import boot.spring.backend.quotes.dto.quotes.QuoteResponsePaginationDto;
import boot.spring.backend.quotes.exception.QuoteAlreadyExistException;
import boot.spring.backend.quotes.exception.QuoteNotFoundException;
import boot.spring.backend.quotes.model.Quote;
import boot.spring.backend.quotes.repository.QuoteRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static boot.spring.backend.quotes.service.cache.CacheConstants.QUOTE_CACHE;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@Service
@CacheConfig(cacheNames = QUOTE_CACHE)
public class QuoteServiceImpl implements QuoteService {
    private static final Logger LOG = LoggerFactory.getLogger(QuoteServiceImpl.class);
    private final QuoteRepository quoteRepository;
    private static final ModelMapper modelMapper = new ModelMapper();

    public QuoteServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    @Cacheable(key = "#id")
    public QuoteResponseDto findQuoteById(Long id) throws QuoteNotFoundException {
        LOG.info("Find quote by id {}", id);
        Quote quote = quoteRepository.findById(id).orElseThrow(QuoteNotFoundException::new);
        return modelMapper.map(quote, QuoteResponseDto.class);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public QuoteResponseDto saveQuote(QuoteRequestDto request) throws QuoteAlreadyExistException {
        if (quoteRepository.existsQuoteByText(request.getText())) {
            throw new QuoteAlreadyExistException();
        }

        Quote quoteToBeSaved = modelMapper.map(request, Quote.class);
        Quote savedQuote = quoteRepository.save(quoteToBeSaved);
        LOG.info("Saved new quote with id {}", savedQuote.getId());
        return modelMapper.map(savedQuote, QuoteResponseDto.class);
    }

    @Override
    @Transactional
    @CachePut(key = "#request.id")
    public QuoteResponseDto updateQuote(QuoteRequestDto request) throws QuoteNotFoundException {
        Quote savedQuote = quoteRepository.findById(request.getId()).orElseThrow(QuoteNotFoundException::new);
        LOG.info("Update quote with id {}", request.getId());
        savedQuote.setText(request.getText());
        Quote updatedQuote = quoteRepository.save(savedQuote);
        return modelMapper.map(updatedQuote, QuoteResponseDto.class);
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteById(Long id) throws QuoteNotFoundException {
        if (!quoteRepository.existsById(id)) {
            throw new QuoteNotFoundException();
        }
        LOG.info("Delete quote with id {}", id);
        quoteRepository.deleteById(id);
    }

    @Override
    public List<QuoteResponseDto> findAll() {
        List<Quote> quotes = quoteRepository.findAll();
        return convertToQuoteResponseDtos(quotes);
    }

    @Override
    @Cacheable(key = "{#page, #pageSize}")
    public QuoteResponsePaginationDto findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Quote> quotePage = quoteRepository.findAll(pageable);
        return createPaginationResponse(quotePage);
    }

    private List<QuoteResponseDto> convertToQuoteResponseDtos(List<Quote> quotes) {
        return quotes.stream()
                .map(quote -> modelMapper.map(quote, QuoteResponseDto.class))
                .toList();
    }

    @Override
    public QuoteResponseDto findRandomQuote() {
        Quote quote = quoteRepository.findRandomQuote();
        return modelMapper.map(quote, QuoteResponseDto.class);
    }

    @Override
    public List<QuoteResponseDto> findQuotesHavingText(String text) {
        List<Quote> quotes = quoteRepository.findByTextContaining(text);
        return convertToQuoteResponseDtos(quotes);
    }

    @Override
    public QuoteResponsePaginationDto findQuotesHavingText(String text, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Quote> quotes = quoteRepository.findByTextContaining(text, pageable);
        return createPaginationResponse(quotes);
    }

    private QuoteResponsePaginationDto createPaginationResponse(Page<Quote> quotes) {
        QuoteResponsePaginationDto response = new QuoteResponsePaginationDto();
        Pageable pageable = quotes.getPageable();
        response.setPage(pageable.getPageNumber());
        response.setPageSize(pageable.getPageSize());
        response.setTotalQuotes(quotes.getTotalElements());
        List<Quote> content = quotes.getContent();
        response.setQuotes(convertToQuoteResponseDtos(content));
        return response;
    }
}
