package boot.spring.backend.quotes.repository;

import boot.spring.backend.quotes.model.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query(value = "SELECT id FROM quote LIMIT 100;", nativeQuery = true)
    List<Long> findLimitedQuoteIds();

    List<Quote> findByTextContaining(String text);

    Page<Quote> findByTextContaining(String text, Pageable pageable);

    boolean existsQuoteByText(String text);

    @Query(value = "SELECT * FROM quotes ORDER BY RAND() LIMIT 1;", nativeQuery = true)
    Quote findRandomQuote();
}
