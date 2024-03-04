package boot.spring.backend.quotes.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class RegisterRequest {
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String password;
    @NotNull
    @NotEmpty
    private String firstName;
    @NotNull
    @NotEmpty
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    public RegisterRequest() {}

    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public static RegisterRequestBuilder builder() {
        return new RegisterRequestBuilder();
    }

    public static class RegisterRequestBuilder {
        private RegisterRequest instance = new RegisterRequest();

        public RegisterRequestBuilder username(String username) {
            this.instance.username = username;
            return this;
        }

        public RegisterRequestBuilder password(String password) {
            this.instance.password = password;
            return this;
        }

        public RegisterRequestBuilder firstName(String firstName) {
            this.instance.firstName = firstName;
            return this;
        }

        public RegisterRequestBuilder lastName(String lastName) {
            this.instance.lastName = lastName;
            return this;
        }

        public RegisterRequestBuilder birthDate(LocalDate birthDate) {
            this.instance.birthDate = birthDate;
            return this;
        }

        public RegisterRequest build() {
            return this.instance;
        }
    }
}
