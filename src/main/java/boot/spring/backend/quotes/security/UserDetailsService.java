package boot.spring.backend.quotes.security;

import boot.spring.backend.quotes.model.User;
import boot.spring.backend.quotes.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    var user = userRepository.findByEmail(username).orElseThrow();

    return User.builder()
        .id(user.getId())
        .email(user.getEmail())
        .role(user.getRole())
        .password(user.getPassword())
        .build();
  }
}
