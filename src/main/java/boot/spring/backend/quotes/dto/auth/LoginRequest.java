package boot.spring.backend.quotes.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class LoginRequest {

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static LoginRequestBuilder builder() {
        return new LoginRequestBuilder();
    }

    public static class LoginRequestBuilder {
        private final LoginRequest instance = new LoginRequest();

        public LoginRequestBuilder email(String email) {
            this.instance.username = email;
            return this;
        }

        public LoginRequestBuilder password(String password) {
            this.instance.password = password;
            return this;
        }

        public LoginRequest build() {
            return instance;
        }
    }
}
