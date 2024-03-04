package boot.spring.backend.quotes.controller;

import boot.spring.backend.quotes.Utils;
import boot.spring.backend.quotes.dto.auth.AuthenticationResponse;
import boot.spring.backend.quotes.dto.auth.LoginRequest;
import boot.spring.backend.quotes.dto.auth.RegisterRequest;
import boot.spring.backend.quotes.exception.UserAlreadyExistsException;
import boot.spring.backend.quotes.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock
  AuthService authService;

  @InjectMocks
  AuthController authController;

  MockMvc mvc;

  final String BASE_URL = "/auth";
  final String LOGIN_URL = BASE_URL.concat("/").concat("login");
  final String REGISTER_URL = BASE_URL.concat("/").concat("register");
  final String REFRESH_TOKEN_URL = BASE_URL.concat("/").concat("refresh-token");
  LoginRequest loginRequest;
  RegisterRequest registerRequest;
  @BeforeEach
  void setUp() {
    String email = "email@test.com";
    String password = "password";
    loginRequest = LoginRequest.builder()
        .email(email)
        .password(password)
        .build();
    registerRequest = RegisterRequest
        .builder()
        .username(RandomString.make())
        .password(RandomString.make())
        .firstName(RandomString.make())
        .lastName(RandomString.make())
        .build();
    mvc = MockMvcBuilders
        .standaloneSetup(authController)
        .setControllerAdvice(new QuoteControllerAdvice())
        .build();
  }

  @Test
  void successfulLogin() throws Exception {
    AuthenticationResponse authResponse = AuthenticationResponse
        .builder()
        .accessToken(RandomString.make())
        .refreshToken(RandomString.make())
        .build();

    when(authService.login(anyString(), anyString())).thenReturn(authResponse);

    mvc.perform(post(LOGIN_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(Utils.toJson(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.access_token").isNotEmpty())
        .andExpect(jsonPath("$.refresh_token").isNotEmpty());
  }

  @Test
  void failedLogin() throws Exception {
    when(authService.login(anyString(), anyString())).thenThrow(InternalAuthenticationServiceException.class);

    mvc.perform(post(LOGIN_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(Utils.toJson(loginRequest)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void failedLoginInvalidRequestThrowsMethodArgumentNotValidException() throws Exception {
    LoginRequest loginRequest = LoginRequest
        .builder()
        .build();

    mvc.perform(post(LOGIN_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(Utils.toJson(loginRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void successfulRegister() throws Exception {
    AuthenticationResponse authResponse = AuthenticationResponse
        .builder()
        .accessToken(RandomString.make())
        .refreshToken(RandomString.make())
        .build();

    when(authService.register(any())).thenReturn(authResponse);

    mvc.perform(post(REGISTER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(Utils.toJson(registerRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.access_token").isNotEmpty())
        .andExpect(jsonPath("$.refresh_token").isNotEmpty());
  }

  @Test
  void failedRegisterInvalidRequestThrowsMethodArgumentNotValidException() throws Exception {
    RegisterRequest registerRequest = RegisterRequest
        .builder()
        .build();

    mvc.perform(post(REGISTER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(Utils.toJson(registerRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void failedRegisterInvalidRequestThrowsUserAlreadyExistsException() throws Exception {

    when(authService.register(any())).thenThrow(UserAlreadyExistsException.class);

    mvc.perform(post(REGISTER_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(Utils.toJson(registerRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @Disabled("TODO")
  void successfulRefreshToken() throws Exception {
    HttpServletResponse response = mock(HttpServletResponse.class);
    AuthenticationResponse authResponse = AuthenticationResponse
        .builder()
        .accessToken(RandomString.make())
        .refreshToken(RandomString.make())
        .build();

    doAnswer(invocation -> {
      String json = new ObjectMapper().writeValueAsString(authResponse);
      response.getOutputStream().write(json.getBytes());
      return null; // Correctly set up for a void method
    }).when(authService).refreshToken(any(), any());

    mvc.perform(post(REFRESH_TOKEN_URL)
            .header(HttpHeaders.AUTHORIZATION, "Bearer ".concat(RandomString.make(20))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.access_token").isNotEmpty())
        .andExpect(jsonPath("$.refresh_token").isNotEmpty());
  }

}