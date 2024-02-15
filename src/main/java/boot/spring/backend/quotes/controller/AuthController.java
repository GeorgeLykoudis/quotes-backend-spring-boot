package boot.spring.backend.quotes.controller;

import boot.spring.backend.quotes.dto.auth.LoginRequest;
import boot.spring.backend.quotes.dto.auth.LoginResponse;
import boot.spring.backend.quotes.security.JwtIssuer;
import boot.spring.backend.quotes.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtIssuer jwtIssuer,
                          AuthenticationManager authenticationManager) {
        this.jwtIssuer = jwtIssuer;
      this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request) {
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

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

    @GetMapping("/secured")
    public String login(@AuthenticationPrincipal UserPrincipal principal) {
        return "if you see this you are logged in as user " + principal.getEmail() +
            " User ID: " + principal.getUserId();
    }
}
