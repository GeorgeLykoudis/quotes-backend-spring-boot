package boot.spring.backend.quotes.service.auth;

import boot.spring.backend.quotes.dto.auth.AuthenticationResponse;
import boot.spring.backend.quotes.dto.auth.RegisterRequest;
import boot.spring.backend.quotes.exception.UserAlreadyExistsException;
import boot.spring.backend.quotes.jwt.JwtHelper;
import boot.spring.backend.quotes.model.UserEntity;
import boot.spring.backend.quotes.model.security.Role;
import boot.spring.backend.quotes.security.UserDetailsService;
import boot.spring.backend.quotes.service.token.TokenService;
import boot.spring.backend.quotes.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
public class AuthService {

  private final JwtHelper jwtHelper;
  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;

  public AuthService(JwtHelper jwtHelper,
                     AuthenticationManager authenticationManager,
                     UserService userService,
                     TokenService tokenService,
                     PasswordEncoder passwordEncoder,
                     UserDetailsService userDetailsService) {
    this.jwtHelper = jwtHelper;
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.tokenService = tokenService;
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
  }

  @Transactional
  public AuthenticationResponse login(String email, String password) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password)
    );

    UserEntity userEntity = userService.findByEmail(email).orElseThrow();
    String token = jwtHelper.generateToken(userEntity);
    String refreshToken = jwtHelper.generateRefreshToken(userEntity);
    tokenService.revokeAllUserTokens(userEntity);
    tokenService.save(token, userEntity);
    return AuthenticationResponse.builder()
        .accessToken(token)
        .refreshToken(refreshToken)
        .build();
  }

  @Transactional
  public AuthenticationResponse register(RegisterRequest request) throws UserAlreadyExistsException {
    if (userService.existsByEmail(request.getUsername())) {
      throw new UserAlreadyExistsException("User already in use");
    }

    UserEntity userEntity = UserEntity.builder()
            .email(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();

    UserEntity savedUser = userService.save(userEntity);
    String token = jwtHelper.generateToken(savedUser);
    String refreshToken = jwtHelper.generateRefreshToken(savedUser);
    tokenService.save(token, savedUser);
    return AuthenticationResponse.builder()
        .accessToken(token)
        .refreshToken(refreshToken)
        .build();
  }

  @Transactional
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Optional<String> tokenOptional = AuthServiceUtils.extractTokenFromRequest(request);
    if (tokenOptional.isEmpty()) {
      return;
    }

    String refreshToken = tokenOptional.get();
    String username = jwtHelper.extractUserName(refreshToken);
    if (username != null) {
      UserEntity userEntity = userDetailsService.loadUserByUsername(username);
      if (jwtHelper.isTokenValid(refreshToken, userEntity)) {
        var accessToken = jwtHelper.generateToken(userEntity);
        tokenService.revokeAllUserTokens(userEntity);
        tokenService.save(accessToken, userEntity);
        var authResponse = AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
