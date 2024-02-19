package boot.spring.backend.quotes.dto.auth;

public class LoginResponse {

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }

    public static class LoginResponseBuilder {
        private final LoginResponse instance = new LoginResponse();

        public LoginResponseBuilder accessToken(String accessToken) {
            this.instance.token = accessToken;
            return this;
        }

        public LoginResponse build() {
            return this.instance;
        }
    }
}
