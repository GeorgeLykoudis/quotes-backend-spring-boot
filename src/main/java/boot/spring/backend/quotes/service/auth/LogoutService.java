package boot.spring.backend.quotes.service.auth;

import boot.spring.backend.quotes.service.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LogoutService implements LogoutHandler {

  private final TokenService tokenService;

  public LogoutService(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  public void logout(HttpServletRequest request,
                     HttpServletResponse response,
                     Authentication authentication) {
    Optional<String> tokenOptional = AuthServiceUtils.extractTokenFromRequest(request);
    if (tokenOptional.isEmpty()) {
      return;
    }
    String token = tokenOptional.get();
    var storedToken = tokenService.findByToken(token).orElse(null);
    if (storedToken != null) {
      storedToken.setExpired(true);
      storedToken.setRevoked(true);
      tokenService.save(storedToken);
    }
  }
}
