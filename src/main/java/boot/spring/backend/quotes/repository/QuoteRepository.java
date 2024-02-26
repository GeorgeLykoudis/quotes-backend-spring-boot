package boot.spring.backend.quotes.repository;

import boot.spring.backend.quotes.model.Quote;
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
public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query(value = "SELECT id FROM quote LIMIT 100;", nativeQuery = true)
    List<Long> findLimitedQuoteIds();

    List<Quote> findByTextContaining(String text);

    Page<Quote> findByTextContaining(String text, Pageable pageable);

    boolean existsQuoteByText(String text);

    @Query(value = "SELECT * FROM quotes ORDER BY RAND() LIMIT 1;", nativeQuery = true)
    Quote findRandomQuote();

    List<Quote> findAllByCreatedBy(String username);

    Page<Quote> findAllByCreatedBy(Pageable pageable, String username);

    Optional<Quote> findByIdAndCreatedBy(Long id, String username);

    boolean existsByIdAndCreatedBy(Long id, String username);
}
