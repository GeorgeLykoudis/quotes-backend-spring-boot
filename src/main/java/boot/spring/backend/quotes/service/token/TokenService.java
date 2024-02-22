package boot.spring.backend.quotes.service.token;

import boot.spring.backend.quotes.model.User;
import boot.spring.backend.quotes.model.security.Token;
import boot.spring.backend.quotes.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenService {

  private final TokenRepository tokenRepository;

  public TokenService(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  public Token save(Token token) {
    return tokenRepository.save(token);
  }

  public Token save(String token, User user) {
    Token tokenEntity = Token.builder()
        .user(user)
        .token(token)
        .expired(false)
        .revoked(false)
        .build();
    return tokenRepository.save(tokenEntity);
  }

  public List<Token> findValidTokensByUser(Long id) {
    return tokenRepository.findAllValidTokensByUser(id);
  }

  public Optional<Token> findByToken(String token) {
    return tokenRepository.findByToken(token);
  }

  public void revokeAllUserTokens(User user) {
    var validUserTokens = findValidTokensByUser(user.getId());
    if (validUserTokens.isEmpty()) {
      return;
    }
    validUserTokens.forEach(t -> {
      t.setRevoked(true);
      t.setExpired(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }
}
