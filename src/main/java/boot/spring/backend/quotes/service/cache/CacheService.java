package boot.spring.backend.quotes.service.cache;

import boot.spring.backend.quotes.exception.QuoteInternalException;

public interface CacheService {

  void clear() throws QuoteInternalException;

}
