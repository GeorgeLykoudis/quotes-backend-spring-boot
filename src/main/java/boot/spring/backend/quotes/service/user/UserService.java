package boot.spring.backend.quotes.service.user;

import boot.spring.backend.quotes.dto.ChangePasswordRequest;
import boot.spring.backend.quotes.exception.ChangePasswordException;
import boot.spring.backend.quotes.model.User;
import boot.spring.backend.quotes.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository,
                     PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public void changePassword(ChangePasswordRequest request, Principal connectedUser) throws ChangePasswordException {
      var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

      // check if the current password is correct
      if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
        throw new ChangePasswordException("Wrong password");
      }

      // check if the two new passwords are the same
      if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
        throw new ChangePasswordException("Passwords are not the same");
      }

      // update password and update user
      user.setPassword(passwordEncoder.encode(request.getNewPassword()));
      userRepository.save(user);
  }
}
