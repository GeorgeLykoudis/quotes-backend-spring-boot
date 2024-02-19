package boot.spring.backend.quotes.jwt;

import boot.spring.backend.quotes.security.UserDetailsService;
import boot.spring.backend.quotes.security.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtHelper jwtHelper;
  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtHelper jwtHelper,
                                 UserDetailsService userDetailsService) {
    this.jwtHelper = jwtHelper;
    this.userDetailsService = userDetailsService;
  }

  @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

      Optional<String> tokenOptional = extractTokenFromRequest(request);
      if (tokenOptional.isEmpty()) {
        filterChain.doFilter(request, response);
        return;
      }

      String token = tokenOptional.get();
      String username = jwtHelper.extractUserName(token);
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserPrincipal userPrincipal = userDetailsService.loadUserByUsername(username);
        if (jwtHelper.isValid(token, userPrincipal)) {
          UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userPrincipal, null, userPrincipal.getAuthorities()
          );

          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
      filterChain.doFilter(request, response);
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }
}
