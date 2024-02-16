package boot.spring.backend.quotes.security;

import boot.spring.backend.quotes.service.user.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

  private final UserService userService;

  public UserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = userService.findByEmail(username).orElseThrow();

    return UserPrincipal.builder()
        .userId(user.getId())
        .email(user.getEmail())
        .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name())))
        .password(user.getPassword())
        .build();
  }
}
