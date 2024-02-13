package boot.spring.backend.quotes.dto.auth;

public class LoginResponse {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }

    public static class LoginResponseBuilder {
        private LoginResponse instance = new LoginResponse();

        public LoginResponseBuilder accessToken(String accessToken) {
            this.instance.accessToken = accessToken;
            return this;
        }

        public LoginResponse build() {
            return this.instance;
        }
    }
}
