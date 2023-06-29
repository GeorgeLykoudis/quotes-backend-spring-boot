package boot.spring.backend.quotes.service.impl;

import boot.spring.backend.quotes.dto.QuoteRequestDto;
import boot.spring.backend.quotes.exception.QuoteNotFoundException;
import boot.spring.backend.quotes.model.QuoteEntity;
import boot.spring.backend.quotes.repository.QuoteRepository;
import boot.spring.backend.quotes.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
