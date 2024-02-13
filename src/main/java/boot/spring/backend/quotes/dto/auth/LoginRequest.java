package boot.spring.backend.quotes.dto.auth;

public class LoginRequest {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
            this.instance.email = email;
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
