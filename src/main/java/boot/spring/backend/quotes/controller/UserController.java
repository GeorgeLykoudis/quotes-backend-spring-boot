package boot.spring.backend.quotes.controller;

import boot.spring.backend.quotes.dto.ChangePasswordRequest;
import boot.spring.backend.quotes.exception.ChangePasswordException;
import boot.spring.backend.quotes.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request,
                                            Principal connectedUser) throws ChangePasswordException {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.accepted().build();
    }
}
