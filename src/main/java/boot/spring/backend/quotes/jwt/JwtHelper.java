package boot.spring.backend.quotes.jwt;

import boot.spring.backend.quotes.model.UserEntity;
import boot.spring.backend.quotes.security.UserPrincipal;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtHelper {
  private static final String JWT_CLAIM_EMAIL = "e";
  private static final String JWT_CLAIM_AUTHORITY = "a";
  private static final Duration JWT_DURATION = Duration.ofDays(1L);
  private static final Date JWT_EXPIRATION = new Date(Instant.now().plus(JWT_DURATION).toEpochMilli());
  private final JwtProperties properties;

  public JwtHelper(JwtProperties properties) {
    this.properties = properties;
  }

  public String generateJwt(UserEntity user) {
    return Jwts
        .builder()
        .setSubject(String.valueOf(user.getId()))
        .setIssuedAt(new Date())
        .setExpiration(JWT_EXPIRATION)
        .signWith(getSigningKey())
        .claim(JWT_CLAIM_EMAIL, user.getEmail())
        .claim(JWT_CLAIM_AUTHORITY, user.getRole())
        .compact();
  }

  public DecodedJWT decodeJwt(String token) {
    return JWT.require(Algorithm.HMAC256(properties.getSecretKey()))
        .build()
        .verify(token);
  }

  public UserPrincipal convert(DecodedJWT jwt) {
    return UserPrincipal.builder()
        .userId(Long.valueOf(jwt.getSubject()))
        .email(jwt.getClaim(JWT_CLAIM_EMAIL).asString())
        .authorities(extractAuthoritiesFromClaim(jwt))
        .build();
  }

  private List<SimpleGrantedAuthority> extractAuthoritiesFromClaim(DecodedJWT jwt) {
    var claim = jwt.getClaim(JWT_CLAIM_AUTHORITY);
    if (claim.isNull() || claim.isMissing()) return List.of();
    return claim.asList(SimpleGrantedAuthority.class);
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(properties.getSecretKey());
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public boolean isValid(String token, UserPrincipal principal) {
    String username = extractUserName(token);
    return username.equals(principal.getUsername()) &&
        !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> resolver) {
    Claims claims = extractAllClaims(token);
    return resolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
