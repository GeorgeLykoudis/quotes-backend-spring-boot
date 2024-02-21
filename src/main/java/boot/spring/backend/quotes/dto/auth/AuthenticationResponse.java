package boot.spring.backend.quotes.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }

    public static class LoginResponseBuilder {
        private final AuthenticationResponse instance = new AuthenticationResponse();

        public LoginResponseBuilder accessToken(String accessToken) {
            this.instance.accessToken = accessToken;
            return this;
        }

        public LoginResponseBuilder refreshToken(String refreshToken) {
            this.instance.refreshToken = refreshToken;
            return this;
        }

        public AuthenticationResponse build() {
            return this.instance;
        }
    }
}
