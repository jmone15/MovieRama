package gr.jmone.movierama.service;

import static gr.jmone.movierama.helper.MockData.createLoginRequestDto;
import static gr.jmone.movierama.helper.MockData.createSignupRequestDto;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gr.jmone.movierama.exception.MovieRamaProcessingException;
import gr.jmone.movierama.model.User;
import gr.jmone.movierama.repository.UserRepository;
import gr.jmone.movierama.security.TokenManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private AuthenticationManager authenticationManager;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private TokenManager tokenManager;
  @InjectMocks private AuthenticationService authenticationService;

  private static final String MOCKED_JWT =
      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMiIsImlhdCI6MTY5NTIzODk1MiwiZXhwIjoxNjk1MzI1MzUyfQ.q_XxUObOwvKcBxyKtvdDgDiz3osZE-LSQNHNniY0Wo8";

  @Test
  void handleUserLogin_getAuthResponse() {
    when(authenticationManager.authenticate(any(Authentication.class)))
        .thenReturn(mock(Authentication.class));
    when(tokenManager.generateJwtToken(any(Authentication.class))).thenReturn(MOCKED_JWT);

    assertThat(authenticationService.handleUserLogin(createLoginRequestDto()).getToken())
        .isEqualTo(MOCKED_JWT);
  }

  @Test
  void handleUserRegistration_getAuthResponse() {
    var user = mock(User.class);
    when(userRepository.existsByEmail(anyString())).thenReturn(false);
    when(userRepository.existsByUsername(anyString())).thenReturn(false);
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(authenticationManager.authenticate(any(Authentication.class)))
        .thenReturn(mock(Authentication.class));
    when(tokenManager.generateJwtToken(any(Authentication.class))).thenReturn(MOCKED_JWT);

    assertThat(authenticationService.handleUserRegistration(createSignupRequestDto()).getToken())
        .isEqualTo(MOCKED_JWT);
  }

  @Test
  void handleUserRegistration_usernameExists_throwError() {
    when(userRepository.existsByUsername(anyString())).thenReturn(true);

    assertThatThrownBy(() -> authenticationService.handleUserRegistration(createSignupRequestDto()))
        .isInstanceOf(MovieRamaProcessingException.class)
        .hasMessage("Error: Username is already taken!");
  }

  @Test
  void handleUserRegistration_emailExists_throwError() {
    when(userRepository.existsByEmail(anyString())).thenReturn(true);

    assertThatThrownBy(() -> authenticationService.handleUserRegistration(createSignupRequestDto()))
        .isInstanceOf(MovieRamaProcessingException.class)
        .hasMessage("Error: Email is already in use!");
  }
}
