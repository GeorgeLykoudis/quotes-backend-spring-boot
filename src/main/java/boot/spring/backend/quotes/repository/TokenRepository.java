package boot.spring.backend.quotes.repository;

import boot.spring.backend.quotes.model.security.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

  @Query(
      value = """
        SELECT t.id, t.token, t.token_type, t.revoked, t.expired, t.user_id AS user_id
        FROM tokens t INNER JOIN users u ON t.user_id = u.id
        WHERE u.id = :userId AND (t.expired = false or t.revoked = false)
      """,
      nativeQuery = true
  )
  List<Token> findAllValidTokensByUser(Long userId);

  Optional<Token> findByToken(String token);

}
