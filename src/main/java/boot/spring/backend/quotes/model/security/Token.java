package boot.spring.backend.quotes.model.security;

import boot.spring.backend.quotes.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "token")
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String token;
  @Enumerated(EnumType.STRING)
  private TokenType tokenType = TokenType.BEARER;
  private boolean revoked;
  private boolean expired;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public Token() {}

  public Token(String token,
               TokenType tokenType,
               boolean revoked,
               boolean expired,
               User user) {
    this.token = token;
    this.tokenType = tokenType;
    this.revoked = revoked;
    this.expired = expired;
    this.user = user;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public TokenType getTokenType() {
    return tokenType;
  }

  public void setTokenType(TokenType tokenType) {
    this.tokenType = tokenType;
  }

  public boolean isRevoked() {
    return revoked;
  }

  public void setRevoked(boolean revoked) {
    this.revoked = revoked;
  }

  public boolean isExpired() {
    return expired;
  }

  public void setExpired(boolean expired) {
    this.expired = expired;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public static TokenBuilder builder() {
    return new TokenBuilder();
  }

  public static class TokenBuilder {
    private final Token instance = new Token();

    public TokenBuilder id(Long id) {
      this.instance.id = id;
      return this;
    }

    public TokenBuilder token(String token) {
      this.instance.token = token;
      return this;
    }

    public TokenBuilder revoked(boolean revoked) {
      this.instance.revoked = revoked;
      return this;
    }

    public TokenBuilder expired(boolean expired) {
      this.instance.expired = expired;
      return this;
    }

    public TokenBuilder user(User user) {
      this.instance.user = user;
      return this;
    }

    public Token build() {
      return this.instance;
    }
  }

}
