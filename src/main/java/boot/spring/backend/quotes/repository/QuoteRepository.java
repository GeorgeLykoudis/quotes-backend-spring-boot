package boot.spring.backend.quotes.repository;

import boot.spring.backend.quotes.model.QuoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author George Lykoudis
 * @date 6/29/2023
 */
public interface QuoteRepository extends JpaRepository<QuoteEntity, Long> {
}
