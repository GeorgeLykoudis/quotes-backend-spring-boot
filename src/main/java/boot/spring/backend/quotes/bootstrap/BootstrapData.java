package boot.spring.backend.quotes.bootstrap;

import boot.spring.backend.quotes.jwt.JwtHelper;
import boot.spring.backend.quotes.model.User;
import boot.spring.backend.quotes.model.UserInfo;
import boot.spring.backend.quotes.model.security.Role;
import boot.spring.backend.quotes.service.token.TokenService;
import boot.spring.backend.quotes.service.user.UserInfoService;
import boot.spring.backend.quotes.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserService userService;
    private final UserInfoService userInfoService;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    private final TokenService tokenService;

    public BootstrapData(UserService userService,
                         UserInfoService userInfoService,
                         PasswordEncoder passwordEncoder,
                         JwtHelper jwtHelper,
                         TokenService tokenService) {
        this.userService = userService;
        this.userInfoService = userInfoService;
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

        UserInfo userInfo = UserInfo.builder()
            .firstName("George")
            .lastName("Lykoudis")
            .birthDate(LocalDate.of(1992, 1, 3))
            .build();
        UserInfo savedUserInfo = userInfoService.save(userInfo);

        User user = User.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .role(Role.ADMIN)
            .userInfo(savedUserInfo)
            .build();

        User savedUser = userService.save(user);
        String token = jwtHelper.generateToken(savedUser);

        tokenService.save(token, savedUser);
    }
}
