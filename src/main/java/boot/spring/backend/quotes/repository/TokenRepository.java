package boot.spring.backend.quotes.repository;

import boot.spring.backend.quotes.model.security.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
}
