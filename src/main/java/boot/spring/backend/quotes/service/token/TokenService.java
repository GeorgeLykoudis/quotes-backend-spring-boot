package boot.spring.backend.quotes.service.token;

import boot.spring.backend.quotes.model.UserEntity;
import boot.spring.backend.quotes.model.security.TokenEntity;
import boot.spring.backend.quotes.repository.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  private final TokenRepository tokenRepository;

  public TokenService(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
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
}
