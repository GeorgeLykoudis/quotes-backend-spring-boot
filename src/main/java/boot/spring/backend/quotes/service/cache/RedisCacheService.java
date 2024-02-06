package boot.spring.backend.quotes.service.cache;

import boot.spring.backend.quotes.exception.QuoteInternalException;
import boot.spring.backend.quotes.model.QuoteEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RedisCacheService implements CacheService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheService.class);

//  private final RedisTemplate<String, QuoteEntity> redisTemplate;
//  private final ValueOperations<String, QuoteEntity> valueOperations;
  private final CacheManager cacheManager;
  private final int TTL;

  public RedisCacheService(//RedisTemplate<String, QuoteEntity> redisTemplate,
                           CacheManager cacheManager,
                           @Value("${redis.ttl.in.seconds}") int ttl) {
//    this.redisTemplate = redisTemplate;
    this.cacheManager = cacheManager;
//    this.valueOperations = this.redisTemplate.opsForValue();
    this.TTL = ttl;
  }

  @Override
  public QuoteEntity findQuoteById(Long id) {
    LOGGER.info("Try to find Quote with id {}", id);
//    return valueOperations.get(id.toString());
    return null;
  }

  @Override
  public void clear() throws QuoteInternalException {
    LOGGER.info("Try to delete every key -- start");

    cacheManager.getCacheNames().forEach(c -> LOGGER.info("cache name {}", c));

//    Cache cache = cacheManager.getCache("0");
//    if (cache == null) {
//      LOGGER.error("Could not clear cache memory");
//      throw new QuoteInternalException("Could not clear cache memory");
//    }
//    cache.clear();
    LOGGER.info("Try to delete every key -- end");
  }

  private boolean hasKey(String id) {
    return false;
//    return Boolean.TRUE.equals(redisTemplate.hasKey(id));
  }

  @Override
  public List<Long> findLimitedQuoteIds() {
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

  @Override
  public void save(QuoteEntity quote) {
    if (quote == null) return;
    LOGGER.info("Save quote with id {} -- start", quote.getId());
//    valueOperations.setIfAbsent(quote.getId().toString(), quote, TTL, TimeUnit.SECONDS);
    LOGGER.info("Save quote with id {} -- end", quote.getId());
  }
}
