package boot.spring.backend.quotes.service.token;

import boot.spring.backend.quotes.model.UserEntity;
import boot.spring.backend.quotes.model.security.TokenEntity;
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

  public TokenEntity save(TokenEntity tokenEntity) {
    return tokenRepository.save(tokenEntity);
  }

  public TokenEntity save(String token, UserEntity user) {
    TokenEntity tokenEntity = TokenEntity.builder()
        .userEntity(user)
        .token(token)
        .expired(false)
        .revoked(false)
        .build();
    return tokenRepository.save(tokenEntity);
  }

  public List<TokenEntity> findValidTokensByUser(Long id) {
    return tokenRepository.findAllValidTokensByUser(id);
  }

  public Optional<TokenEntity> findByToken(String token) {
    return tokenRepository.findByToken(token);
  }

  public void revokeAllUserTokens(UserEntity user) {
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
