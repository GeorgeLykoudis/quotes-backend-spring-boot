package boot.spring.backend.quotes.jwt;

import boot.spring.backend.quotes.model.UserEntity;
import boot.spring.backend.quotes.security.UserDetailsService;
import boot.spring.backend.quotes.service.auth.AuthServiceUtils;
import boot.spring.backend.quotes.service.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtHelper jwtHelper;
  private final UserDetailsService userDetailsService;
  private final TokenService tokenService;

  public JwtAuthenticationFilter(JwtHelper jwtHelper,
                                 UserDetailsService userDetailsService,
                                 TokenService tokenService) {
    this.jwtHelper = jwtHelper;
    this.userDetailsService = userDetailsService;
    this.tokenService = tokenService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
          throws ServletException, IOException {
    Optional<String> tokenOptional = AuthServiceUtils.extractTokenFromRequest(request);
    if (tokenOptional.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = tokenOptional.get();
    String username = jwtHelper.extractUserName(token);
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserEntity userEntity = userDetailsService.loadUserByUsername(username);
      boolean isTokenValid = tokenService.findByToken(token)
          .map(t -> !t.isExpired() && !t.isRevoked())
          .orElse(false);
      if (jwtHelper.isTokenValid(token, userEntity) && isTokenValid) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userEntity, null, userEntity.getAuthorities()
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
    filterChain.doFilter(request, response);
  }
}
