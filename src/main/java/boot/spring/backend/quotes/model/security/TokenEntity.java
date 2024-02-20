package boot.spring.backend.quotes.model.security;

import boot.spring.backend.quotes.model.UserEntity;
import jakarta.persistence.CascadeType;
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
public class TokenEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String token;
  @Enumerated(EnumType.STRING)
  private TokenType tokenType = TokenType.BEARER;
  private boolean revoked;
  private boolean expired;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private UserEntity userEntity;

  public TokenEntity() {}

  public TokenEntity(String token,
                     TokenType tokenType,
                     boolean revoked,
                     boolean expired,
                     UserEntity userEntity) {
    this.token = token;
    this.tokenType = tokenType;
    this.revoked = revoked;
    this.expired = expired;
    this.userEntity = userEntity;
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

  public UserEntity getUserEntity() {
    return userEntity;
  }

  public void setUserEntity(UserEntity userEntity) {
    this.userEntity = userEntity;
  }

  public static TokenEntityBuilder builder() {
    return new TokenEntityBuilder();
  }

  public static class TokenEntityBuilder {
    private final TokenEntity instance = new TokenEntity();

    public TokenEntityBuilder id(Long id) {
      this.instance.id = id;
      return this;
    }

    public TokenEntityBuilder token(String token) {
      this.instance.token = token;
      return this;
    }

    public TokenEntityBuilder revoked(boolean revoked) {
      this.instance.revoked = revoked;
      return this;
    }

    public TokenEntityBuilder expired(boolean expired) {
      this.instance.expired = expired;
      return this;
    }

    public TokenEntityBuilder userEntity(UserEntity userEntity) {
      this.instance.userEntity = userEntity;
      return this;
    }

    public TokenEntity build() {
      return this.instance;
    }
  }

}
