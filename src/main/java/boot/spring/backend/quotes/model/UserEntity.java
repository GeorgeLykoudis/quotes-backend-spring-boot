package boot.spring.backend.quotes.model;

import boot.spring.backend.quotes.model.security.Role;
import boot.spring.backend.quotes.model.security.TokenEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String email;
  @JsonIgnore
  private String password;
  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private Role role;
  @OneToMany(mappedBy = "userEntity")
  private List<TokenEntity> tokens;
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public List<TokenEntity> getTokens() {
    return tokens;
  }

  public void setTokens(List<TokenEntity> tokens) {
    this.tokens = tokens;
  }

  public static UserEntityBuilder builder() {
    return new UserEntityBuilder();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public static class UserEntityBuilder {
    private final UserEntity instance = new UserEntity();

    public UserEntityBuilder id(Long id) {
      this.instance.id = id;
      return this;
    }

    public UserEntityBuilder email(String email) {
      this.instance.email = email;
      return this;
    }

    public UserEntityBuilder password(String password) {
      this.instance.password = password;
      return this;
    }

    public UserEntityBuilder role(Role role) {
      this.instance.role = role;
      return this;
    }

    public UserEntityBuilder tokens(List<TokenEntity> tokens) {
      this.instance.tokens = tokens;
      return this;
    }

    public UserEntity build() {
      return this.instance;
    }
  }
}
