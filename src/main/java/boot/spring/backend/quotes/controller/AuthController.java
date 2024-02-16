package boot.spring.backend.quotes.controller;

import boot.spring.backend.quotes.dto.auth.LoginRequest;
import boot.spring.backend.quotes.dto.auth.LoginResponse;
import boot.spring.backend.quotes.security.UserPrincipal;
import boot.spring.backend.quotes.service.auth.AuthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static boot.spring.backend.quotes.model.security.Persmissions.ADMIN_PERMISSIONS;

@RestController
public class AuthController {

    private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/auth/login")
  public LoginResponse login(@RequestBody @Validated LoginRequest request) {
      return authService.attemptLogin(request.getEmail(), request.getPassword());
  }

  @GetMapping("/secured")
  public String login(@AuthenticationPrincipal UserPrincipal principal) {
      return "if you see this you are logged in as user " + principal.getEmail() +
          " User ID: " + principal.getUserId();
  }

  @GetMapping("/admin")
  @PreAuthorize("hasAuthorize('" + ADMIN_PERMISSIONS + "')")
  public String admin(@AuthenticationPrincipal UserPrincipal principal) {
    return "if you see this you are an admin with email " + principal.getEmail() +
      " User ID: " + principal.getUserId();
  }
}
