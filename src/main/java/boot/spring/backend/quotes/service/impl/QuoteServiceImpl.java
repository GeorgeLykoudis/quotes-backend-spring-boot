package boot.spring.backend.quotes.service.impl;

import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.dto.QuoteResponsePaginationDto;
import boot.spring.backend.quotes.exception.QuoteAlreadyExistException;
import boot.spring.backend.quotes.exception.QuoteNotFoundException;
import boot.spring.backend.quotes.model.QuoteEntity;
import boot.spring.backend.quotes.repository.QuoteRepository;
import boot.spring.backend.quotes.service.QuoteService;
import org.modelmapper.ModelMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static boot.spring.backend.quotes.service.cache.CacheConstants.QUOTE_CACHE;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@Service
public class QuoteServiceImpl implements QuoteService {
    private static final Logger LOG = LoggerFactory.getLogger(QuoteServiceImpl.class);
    private final QuoteRepository quoteRepository;
    private static final ModelMapper modelMapper = new ModelMapper();

    public QuoteServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    @Cacheable(value = QUOTE_CACHE, key = "#id")
    public QuoteResponseDto findQuoteById(Long id) throws QuoteNotFoundException {
        LOG.info("Find quote by id {}", id);
        QuoteEntity quote = quoteRepository.findById(id).orElseThrow(QuoteNotFoundException::new);
        return modelMapper.map(quote, QuoteResponseDto.class);
    }

    @Override
    @Transactional
    @CacheEvict(value = QUOTE_CACHE, allEntries = true)
    public QuoteResponseDto saveQuote(QuoteRequestDto request) {
        if (quoteRepository.existsQuoteByText(request.getText())) {
            throw new QuoteAlreadyExistException();
        }

        QuoteEntity quoteToBeSaved = modelMapper.map(request, QuoteEntity.class);
        QuoteEntity savedQuote = quoteRepository.save(quoteToBeSaved);
        LOG.info("Saved new quote with id {}", savedQuote.getId());
        return modelMapper.map(savedQuote, QuoteResponseDto.class);
    }

    @Override
    @Transactional
    @CachePut(value = QUOTE_CACHE, key = "#quoteRequestDto.id")
    public QuoteResponseDto updateQuote(QuoteRequestDto quoteRequestDto) {
        LOG.info("Update quote with id {}", quoteRequestDto.getId());
        QuoteEntity quote = modelMapper.map(quoteRequestDto, QuoteEntity.class);
        QuoteEntity savedQuote = quoteRepository.save(quote);
        return modelMapper.map(savedQuote, QuoteResponseDto.class);
    }

    @Override
    @CacheEvict(value = QUOTE_CACHE, allEntries = true)
    public void deleteById(Long id) {
        if (!quoteRepository.existsById(id)) {
            throw new QuoteNotFoundException();
        }
        LOG.info("Delete quote with id {}", id);
        quoteRepository.deleteById(id);
    }

    @Override
    public List<QuoteResponseDto> findAll() {
        List<QuoteEntity> quotes = new ArrayList<>();
        return convertToQuoteResponseDtos(quotes);
    }

    @Override
    public QuoteResponsePaginationDto findAll(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<QuoteEntity> quotePage = null;
        return createPaginationResponse(quotePage);
    }

    private List<QuoteResponseDto> convertToQuoteResponseDtos(List<QuoteEntity> quotes) {
        return quotes.stream()
                .map(quote -> modelMapper.map(quote, QuoteResponseDto.class))
                .collect(Collectors.toList());
    }

    @Deprecated
    public QuoteResponseDto findRandomQuote2() {
        List<Long> quoteIds = new ArrayList<>();
        if (quoteIds.isEmpty()) {
            return new QuoteResponseDto();
        }
        int randomNumber = getRandomNumber(quoteIds.size());
        LOG.info("Random Quote:");
        QuoteEntity quote =new QuoteEntity();
        return modelMapper.map(quote, QuoteResponseDto.class);
    }

    private int getRandomNumber(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    @Override
    public QuoteResponseDto findRandomQuote() {
        QuoteEntity quote = quoteRepository.findRandomQuote();
        return modelMapper.map(quote, QuoteResponseDto.class);
    }

    @Override
    public List<QuoteResponseDto> findQuotesHavingText(String text) {
        List<QuoteEntity> quotes = new ArrayList<>();
        return convertToQuoteResponseDtos(quotes);
    }

    @Override
    public QuoteResponsePaginationDto findQuotesHavingText(String text, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<QuoteEntity> quotes = null;
        return createPaginationResponse(quotes);
    }

    private QuoteResponsePaginationDto createPaginationResponse(Page<QuoteEntity> quotes) {
        QuoteResponsePaginationDto response = new QuoteResponsePaginationDto();
        Pageable pageable = quotes.getPageable();
        response.setPage(pageable.getPageNumber());
        response.setPageSize(pageable.getPageSize());
        response.setTotalQuotes(quotes.getTotalElements());
        List<QuoteEntity> content = quotes.getContent();
        response.setQuotes(convertToQuoteResponseDtos(content));
        return response;
    }
}
