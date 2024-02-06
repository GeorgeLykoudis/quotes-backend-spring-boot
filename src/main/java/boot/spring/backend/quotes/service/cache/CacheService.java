package boot.spring.backend.quotes.service.cache;

import boot.spring.backend.quotes.exception.QuoteInternalException;
import boot.spring.backend.quotes.model.QuoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CacheService {

  QuoteEntity findQuoteById(Long id);

  void clear() throws QuoteInternalException;

  List<Long> findLimitedQuoteIds();

  List<QuoteEntity> findAll();

  Page<QuoteEntity> findAll(Pageable pageable);

  List<QuoteEntity> findQuotesHavingText(String text);

  Page<QuoteEntity> findQuotesHavingText(String text, Pageable pageable);

  void save(QuoteEntity quote);

}
