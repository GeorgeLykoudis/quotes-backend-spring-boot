package boot.spring.backend.quotes.service.auth;

import boot.spring.backend.quotes.dto.auth.AuthenticationResponse;
import boot.spring.backend.quotes.dto.auth.RegisterRequest;
import boot.spring.backend.quotes.exception.UserAlreadyExistsException;
import boot.spring.backend.quotes.jwt.JwtHelper;
import boot.spring.backend.quotes.model.User;
import boot.spring.backend.quotes.model.UserInfo;
import boot.spring.backend.quotes.model.security.Role;
import boot.spring.backend.quotes.security.UserDetailsService;
import boot.spring.backend.quotes.service.token.TokenService;
import boot.spring.backend.quotes.service.user.UserInfoService;
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
  private final UserInfoService userInfoService;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;

  public AuthService(JwtHelper jwtHelper,
                     AuthenticationManager authenticationManager,
                     UserService userService,
                     UserInfoService userInfoService,
                     TokenService tokenService,
                     PasswordEncoder passwordEncoder,
                     UserDetailsService userDetailsService) {
    this.jwtHelper = jwtHelper;
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.userInfoService = userInfoService;
    this.tokenService = tokenService;
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
  }

  @Transactional
  public AuthenticationResponse login(String email, String password) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, password)
    );

    User user = userService.findByEmail(email).orElseThrow();
    String token = jwtHelper.generateToken(user);
    String refreshToken = jwtHelper.generateRefreshToken(user);
    tokenService.revokeAllUserTokens(user);
    tokenService.save(token, user);
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

    User savedUser = userService.save(createUser(request));
    String token = jwtHelper.generateToken(savedUser);
    String refreshToken = jwtHelper.generateRefreshToken(savedUser);
    tokenService.save(token, savedUser);
    return AuthenticationResponse.builder()
        .accessToken(token)
        .refreshToken(refreshToken)
        .build();
  }

  @Transactional
  public User createUser(RegisterRequest request) {
    UserInfo userInfo = UserInfo.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .birthDate(request.getBirthDate())
        .build();
    UserInfo savedUserInfo = userInfoService.save(userInfo);

    return User.builder()
        .email(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .userInfo(savedUserInfo)
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
      User user = userDetailsService.loadUserByUsername(username);
      if (jwtHelper.isTokenValid(refreshToken, user)) {
        var accessToken = jwtHelper.generateToken(user);
        tokenService.revokeAllUserTokens(user);
        tokenService.save(accessToken, user);
        var authResponse = AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
