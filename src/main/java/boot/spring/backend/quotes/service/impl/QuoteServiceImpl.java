package boot.spring.backend.quotes.service.impl;

import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.dto.QuoteResponseDto;
import boot.spring.backend.quotes.exception.QuoteNotFoundException;
import boot.spring.backend.quotes.model.QuoteEntity;
import boot.spring.backend.quotes.repository.QuoteRepository;
import boot.spring.backend.quotes.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
@Service
public class QuoteServiceImpl implements QuoteService {
    private static final Logger LOG = LoggerFactory.getLogger(QuoteServiceImpl.class);
    private final QuoteRepository quoteRepository;

    public QuoteServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    @Transactional
    public QuoteEntity saveQuote(QuoteRequestDto quoteRequestDto) {
        QuoteEntity quoteEntity = QuoteEntity.createQuoteEntity(quoteRequestDto);
        QuoteEntity savedQuote = quoteRepository.save(quoteEntity);
        LOG.info("Saved new quote with id {}", savedQuote.getId());
        return savedQuote;
    }

    @Override
    public QuoteEntity findQuoteById(Long id) {
        LOG.info("Find quote by id {}", id);
        return quoteRepository.findById(id).orElseThrow(() -> new QuoteNotFoundException(id));
    }

    @Override
    @Transactional
    public QuoteEntity updateQuoteById(Long id, QuoteRequestDto quoteRequestDto) {
        QuoteEntity quote = findQuoteById(id);
        LOG.info("Update quote with id {}", id);
        quote.setAuthor(quoteRequestDto.getAuthor());
        quote.setText(quoteRequestDto.getText());
        return quoteRepository.save(quote);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!quoteRepository.existsById(id)) {
            throw new QuoteNotFoundException(id);
        }
        LOG.info("Delete quote with id {}", id);
        quoteRepository.deleteById(id);
    }

    @Override
    public List<QuoteEntity> findAll() {
        return quoteRepository.findAll();
    }

    public List<QuoteResponseDto> findAllDtos() {
        List<QuoteEntity> quotes = findAll();
        List<QuoteResponseDto> quotesResponse = new ArrayList<>();

        for (QuoteEntity quote : quotes) {
            quotesResponse.add(QuoteResponseDto.createQuote(quote));
        }
        return quotesResponse;
    }
}
