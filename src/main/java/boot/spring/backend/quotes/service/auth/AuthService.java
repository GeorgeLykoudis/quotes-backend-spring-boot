package boot.spring.backend.quotes.service.auth;

import boot.spring.backend.quotes.dto.auth.LoginResponse;
import boot.spring.backend.quotes.dto.auth.RegisterRequest;
import boot.spring.backend.quotes.exception.UserAlreadyExistsException;
import boot.spring.backend.quotes.model.UserEntity;
import boot.spring.backend.quotes.model.security.Role;
import boot.spring.backend.quotes.security.JwtIssuer;
import boot.spring.backend.quotes.security.UserPrincipal;
import boot.spring.backend.quotes.service.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final JwtIssuer jwtIssuer;
  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public AuthService(JwtIssuer jwtIssuer,
                     AuthenticationManager authenticationManager,
                     UserService userService,
                     PasswordEncoder passwordEncoder) {
    this.jwtIssuer = jwtIssuer;
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  public LoginResponse attemptLogin(String email, String password) {
    var authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    var principal = (UserPrincipal) authentication.getPrincipal();
    var roles = principal.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);

    return LoginResponse.builder()
        .accessToken(token)
        .build();
  }

  public String attemptRegister(RegisterRequest request) throws UserAlreadyExistsException {
    if (userService.existsByEmail(request.getUsername())) {
      throw new UserAlreadyExistsException("User already in use");
    }

    UserEntity userEntity = UserEntity.builder()
            .email(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.ROLE_USER)
            .build();

    userService.save(userEntity);
    return "User register success";
  }
}
