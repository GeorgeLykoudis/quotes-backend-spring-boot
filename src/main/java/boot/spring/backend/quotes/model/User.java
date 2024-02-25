package boot.spring.backend.quotes.model;

import boot.spring.backend.quotes.model.security.Role;
import boot.spring.backend.quotes.model.security.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

  @Column(name = "email")
  private String email;
  @JsonIgnore
  @Column(name = "password")
  private String password;
  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private Role role;
  @OneToMany(mappedBy = "user")
  private List<Token> tokens;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_info_id", referencedColumnName = "id")
  private UserInfo userInfo;

  public User() { super(); }

  public User(Long id) { super(id); }

  public User(String email, String password, Role role, List<Token> tokens) {
    super();
    this.email = email;
    this.password = password;
    this.role = role;
    this.tokens = tokens;
  }

  public User(Long id, String email, String password, Role role, List<Token> tokens) {
    super(id);
    this.email = email;
    this.password = password;
    this.role = role;
    this.tokens = tokens;
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

  public List<Token> getTokens() {
    return tokens;
  }

  public void setTokens(List<Token> tokens) {
    this.tokens = tokens;
  }

  public UserInfo getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(UserInfo userInfo) {
    this.userInfo = userInfo;
  }

  public static UserBuilder builder() {
    return new UserBuilder();
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

  public static class UserBuilder {
    private final User instance = new User();

    public UserBuilder id(Long id) {
      this.instance.setId(id);
      return this;
    }

    public UserBuilder email(String email) {
      this.instance.email = email;
      return this;
    }

    public UserBuilder password(String password) {
      this.instance.password = password;
      return this;
    }

    public UserBuilder role(Role role) {
      this.instance.role = role;
      return this;
    }

    public UserBuilder tokens(List<Token> tokens) {
      this.instance.tokens = new ArrayList<>(tokens);
      return this;
    }

    public UserBuilder userInfo(UserInfo userInfo) {
      this.instance.userInfo = userInfo;
      return this;
    }

    public User build() {
      return this.instance;
    }
  }
}
