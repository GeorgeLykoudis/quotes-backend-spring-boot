package boot.spring.backend.quotes.service.cache;

import boot.spring.backend.quotes.model.QuoteEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisCacheService implements CacheService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheService.class);

  private final RedisTemplate<String, QuoteEntity> redisTemplate;
  private final ValueOperations<String, QuoteEntity> valueOperations;

  public RedisCacheService(RedisTemplate<String, QuoteEntity> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.valueOperations = this.redisTemplate.opsForValue();
  }

  @Override
  public QuoteEntity getQuoteById(Long id) {
    LOGGER.info("Try to find Quote with id {}", id);
    return valueOperations.get(id.toString());
  }

  private boolean hasKey(String id) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(id));
  }

  @Override
  public List<Long> getLimitedQuoteIds() {
    return null;
  }

  @Override
  public List<QuoteEntity> findAll() {
    return null;
  }

  @Override
  public Page<QuoteEntity> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public List<QuoteEntity> findQuotesHavingText(String text) {
    return null;
  }

  @Override
  public Page<QuoteEntity> findQuotesHavingText(String text, Pageable pageable) {
    return null;
  }
}
