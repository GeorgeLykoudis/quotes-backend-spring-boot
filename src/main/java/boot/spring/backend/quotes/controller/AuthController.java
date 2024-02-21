package boot.spring.backend.quotes.controller;

import boot.spring.backend.quotes.dto.auth.AuthenticationResponse;
import boot.spring.backend.quotes.dto.auth.LoginRequest;
import boot.spring.backend.quotes.dto.auth.RegisterRequest;
import boot.spring.backend.quotes.exception.UserAlreadyExistsException;
import boot.spring.backend.quotes.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> login(@RequestBody @Validated LoginRequest request) {
      return ResponseEntity.ok(authService.login(request.getUsername(), request.getPassword()));
  }

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws UserAlreadyExistsException {
    return ResponseEntity.ok(authService.register(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    authService.refreshToken(request, response);
  }
}
