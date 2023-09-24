package gr.jmone.movierama.controller;

import gr.jmone.movierama.dto.AuthResponseDto;
import gr.jmone.movierama.dto.LoginRequestDto;
import gr.jmone.movierama.dto.SignUpRequestDto;
import gr.jmone.movierama.service.AuthenticationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticationService authenticationService;

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
    return ResponseEntity.ok().body(authenticationService.handleUserLogin(loginRequest));
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/signup")
  public ResponseEntity<AuthResponseDto> signUp(
      @Valid @RequestBody SignUpRequestDto signUpRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(authenticationService.handleUserRegistration(signUpRequest));
  }
}
