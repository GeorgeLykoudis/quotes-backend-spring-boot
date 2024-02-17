package boot.spring.backend.quotes.service.user;

import boot.spring.backend.quotes.model.UserEntity;
import boot.spring.backend.quotes.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Optional<UserEntity> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public UserEntity save(UserEntity userEntity) {
    return userRepository.save(userEntity);
  }
}
