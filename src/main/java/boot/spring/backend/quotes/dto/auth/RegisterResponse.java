package boot.spring.backend.quotes.dto.auth;

public class RegisterResponse {
  private String token;

  public RegisterResponse(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }
}
