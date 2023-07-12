package boot.spring.backend.quotes.repository;

import boot.spring.backend.quotes.model.QuoteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
public interface QuoteRepository extends JpaRepository<QuoteEntity, Long> {
    @Query(value = "SELECT id FROM quote LIMIT 100;", nativeQuery = true)
    List<Long> findLimitedQuoteIds();

    List<QuoteEntity> findByTextContaining(String text);

    Page<QuoteEntity> findByTextContaining(String text, Pageable pageable);

    Optional<QuoteEntity> findQuoteByText(String text);

    @Query(value = "SELECT * FROM quote ORDER BY RAND() LIMIT 1;", nativeQuery = true)
    QuoteEntity findRandomQuote();
}
