package boot.spring.backend.quotes.service.cache;

import boot.spring.backend.quotes.exception.QuoteInternalException;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheService implements CacheService {

  private final CacheManager cacheManager;

  public RedisCacheService(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @Override
  public void clear() throws QuoteInternalException {
    cacheManager.getCacheNames()
        .forEach(name -> cacheManager.getCache(name).clear());
  }
}
