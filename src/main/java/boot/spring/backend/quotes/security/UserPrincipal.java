package boot.spring.backend.quotes.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrincipal implements UserDetails {

    private Long userId;
    private String email;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
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

    public Long getUserId() {
        return userId;
    }

    public static UserPrincipalBuilder builder() {
        return new UserPrincipalBuilder();
    }

    public static class UserPrincipalBuilder {
        private final UserPrincipal instance = new UserPrincipal();

        public UserPrincipalBuilder userId(Long userId) {
            this.instance.userId = userId;
            return this;
        }

        public UserPrincipalBuilder email(String email) {
            this.instance.email = email;
            return this;
        }

        public UserPrincipalBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.instance.authorities = authorities;
            return this;
        }

        public UserPrincipalBuilder password(String password) {
            this.instance.password = password;
            return this;
        }

        public UserPrincipal build() {
            return this.instance;
        }
    }
}
