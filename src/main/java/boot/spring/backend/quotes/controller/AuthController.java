package boot.spring.backend.quotes.controller;

import boot.spring.backend.quotes.dto.auth.LoginRequest;
import boot.spring.backend.quotes.dto.auth.LoginResponse;
import boot.spring.backend.quotes.dto.auth.RegisterRequest;
import boot.spring.backend.quotes.exception.UserAlreadyExistsException;
import boot.spring.backend.quotes.service.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/login")
  public LoginResponse login(@RequestBody @Validated LoginRequest request) {
      return authService.attemptLogin(request.getEmail(), request.getPassword());
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterRequest request) throws UserAlreadyExistsException {
    return ResponseEntity.ok(authService.attemptRegister(request));
  }
}
