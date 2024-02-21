package boot.spring.backend.quotes.jwt;

import boot.spring.backend.quotes.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtHelper {
  private static final String JWT_CLAIM_AUTHORITY = "a";
  private final JwtProperties properties;

  public JwtHelper(JwtProperties properties) {
    this.properties = properties;
  }

  public String generateToken(UserEntity user) {
    return buildToken(user, properties.getExpiration());
  }

  public String generateRefreshToken(UserEntity user) {
    return buildToken(user, properties.getRefreshTokenExpiration());
  }

  private String buildToken(UserEntity user, long expiration) {
    return Jwts
        .builder()
        .setSubject(user.getEmail())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .claim(JWT_CLAIM_AUTHORITY, user.getRole())
        .compact();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(properties.getSecretKey());
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public boolean isTokenValid(String token, UserEntity userEntity) {
    String username = extractUserName(token);
    return username.equals(userEntity.getUsername()) &&
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
