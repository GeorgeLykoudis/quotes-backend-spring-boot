package boot.spring.backend.quotes.service.cache;

import boot.spring.backend.quotes.model.QuoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CacheService {

  QuoteEntity getQuoteById(Long id);

  List<Long> getLimitedQuoteIds();

  List<QuoteEntity> findAll();

  Page<QuoteEntity> findAll(Pageable pageable);

  List<QuoteEntity> findQuotesHavingText(String text);

  Page<QuoteEntity> findQuotesHavingText(String text, Pageable pageable);

}
