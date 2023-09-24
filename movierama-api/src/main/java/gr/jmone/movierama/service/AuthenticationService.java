package gr.jmone.movierama.service;

import static gr.jmone.movierama.security.TokenManager.DEFAULT_ROLE;

import gr.jmone.movierama.dto.AuthResponseDto;
import gr.jmone.movierama.dto.LoginRequestDto;
import gr.jmone.movierama.dto.SignUpRequestDto;
import gr.jmone.movierama.exception.MovieRamaProcessingException;
import gr.jmone.movierama.model.User;
import gr.jmone.movierama.repository.UserRepository;
import gr.jmone.movierama.security.TokenManager;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthenticationService {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final TokenManager tokenManager;

  public AuthResponseDto handleUserLogin(LoginRequestDto loginRequest) {
    var token = authenticateAndGetToken(loginRequest.getUsername(), loginRequest.getPassword());
    return AuthResponseDto.builder().token(token).build();
  }

  public AuthResponseDto handleUserRegistration(SignUpRequestDto signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      throw new MovieRamaProcessingException("Error: Username is already taken!");
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new MovieRamaProcessingException("Error: Email is already in use!");
    }

    User user =
        User.builder()
            .username(signUpRequest.getUsername())
            .email(signUpRequest.getEmail())
            .externalId(UUID.randomUUID())
            .password(passwordEncoder.encode(signUpRequest.getPassword()))
            .role(DEFAULT_ROLE)
            .name(signUpRequest.getName())
            .build();

    userRepository.save(user);

    var token = authenticateAndGetToken(signUpRequest.getUsername(), signUpRequest.getPassword());
    return AuthResponseDto.builder().token(token).build();
  }

  private String authenticateAndGetToken(String username, String password) {
    var authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return tokenManager.generateJwtToken(authentication);
  }
}
