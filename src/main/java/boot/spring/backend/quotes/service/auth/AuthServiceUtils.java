package boot.spring.backend.quotes.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class AuthServiceUtils {

  public static Optional<String> extractTokenFromRequest(HttpServletRequest request) {
    var token = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
      return Optional.of(token.substring(7));
    }
    return Optional.empty();
  }

}
