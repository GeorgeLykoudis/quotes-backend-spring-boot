package boot.spring.backend.quotes.repository;

import boot.spring.backend.quotes.model.QuoteEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
public interface QuoteRepository extends JpaRepository<QuoteEntity, Long> {
    @Query(value = "SELECT id FROM quote LIMIT 100;", nativeQuery = true)
    List<Long> findLimitedQuoteIds();

    List<QuoteEntity> findByTextContaining(String text);

    List<QuoteEntity> findByTextContaining(String text, Pageable pageable);

    QuoteEntity findQuoteByText(String text);
}
