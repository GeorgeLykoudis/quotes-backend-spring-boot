package boot.spring.backend.quotes.service.auth;

import boot.spring.backend.quotes.dto.auth.LoginResponse;
import boot.spring.backend.quotes.dto.auth.RegisterRequest;
import boot.spring.backend.quotes.dto.auth.RegisterResponse;
import boot.spring.backend.quotes.exception.UserAlreadyExistsException;
import boot.spring.backend.quotes.jwt.JwtHelper;
import boot.spring.backend.quotes.model.UserEntity;
import boot.spring.backend.quotes.model.security.Role;
import boot.spring.backend.quotes.service.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final JwtHelper jwtHelper;
  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public AuthService(JwtHelper jwtHelper,
                     AuthenticationManager authenticationManager,
                     UserService userService,
                     PasswordEncoder passwordEncoder) {
    this.jwtHelper = jwtHelper;
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  public LoginResponse login(String email, String password) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password)
    );

    UserEntity userEntity = userService.findByEmail(email).orElseThrow();
    String token = jwtHelper.generateJwt(userEntity);

    return LoginResponse.builder()
        .accessToken(token)
        .build();
  }

  public RegisterResponse register(RegisterRequest request) throws UserAlreadyExistsException {
    if (userService.existsByEmail(request.getUsername())) {
      throw new UserAlreadyExistsException("User already in use");
    }

    UserEntity userEntity = UserEntity.builder()
            .email(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.ROLE_USER)
            .build();

    UserEntity savedUser = userService.save(userEntity);
    String token = jwtHelper.generateJwt(savedUser);
    return new RegisterResponse(token);
  }
}
