package boot.spring.backend.quotes.bootstrap;

import boot.spring.backend.quotes.jwt.JwtHelper;
import boot.spring.backend.quotes.model.UserEntity;
import boot.spring.backend.quotes.model.security.Role;
import boot.spring.backend.quotes.service.token.TokenService;
import boot.spring.backend.quotes.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    private final TokenService tokenService;

    public BootstrapData(UserService userService,
                         PasswordEncoder passwordEncoder,
                         JwtHelper jwtHelper,
                         TokenService tokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        final String email = "admin@gmail.com";
        final String password = "adminPassword";
        if (userService.existsByEmail(email)) {
            return;
        }
        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.ADMIN)
                .build();

        UserEntity savedUser = userService.save(userEntity);
        String token = jwtHelper.generateToken(savedUser);

        tokenService.save(token, savedUser);
    }
}
