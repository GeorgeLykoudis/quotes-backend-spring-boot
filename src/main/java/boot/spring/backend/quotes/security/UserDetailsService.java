package boot.spring.backend.quotes.security;

import boot.spring.backend.quotes.model.UserEntity;
import boot.spring.backend.quotes.service.user.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

  private final UserService userService;

  public UserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = userService.findByEmail(username).orElseThrow();

    return UserEntity.builder()
        .id(user.getId())
        .email(user.getEmail())
        .role(user.getRole())
        .password(user.getPassword())
        .build();
  }
}
