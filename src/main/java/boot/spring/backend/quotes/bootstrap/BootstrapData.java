package boot.spring.backend.quotes.bootstrap;

import boot.spring.backend.quotes.model.UserEntity;
import boot.spring.backend.quotes.model.security.Role;
import boot.spring.backend.quotes.service.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public BootstrapData(UserService userService,
                         PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String email = "admin@gmail.com";
        if (userService.existsByEmail(email)) {
            return;
        }
        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode("adminPassword"))
                .role(Role.ROLE_ADMIN)
                .build();
        userService.save(userEntity);
    }
}
