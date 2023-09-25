package gr.jmone.movierama.controller.v1;

import static gr.jmone.movierama.config.WebSecurityConfig.BEARER_KEY_SECURITY_SCHEME;

import gr.jmone.movierama.dto.MovieDto;
import gr.jmone.movierama.dto.MovieDtoBase;
import gr.jmone.movierama.dto.MovieQueryDto;
import gr.jmone.movierama.security.UserDetailsImpl;
import gr.jmone.movierama.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movies")
public class MovieController {

  private final MovieService movieService;

  @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
  @PreAuthorize("hasRole('ROLE_USER')")
  @PostMapping
  public ResponseEntity<MovieDto> createMovie(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @Valid @RequestBody MovieDtoBase movieDtoBase) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(movieService.saveMovie(userDetails.getExternalId(), movieDtoBase));
  }

  @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
  @GetMapping
  public ResponseEntity<List<MovieDto>> getMovies(MovieQueryDto movieQuery) {
    return ResponseEntity.ok().body(movieService.getMovies(movieQuery));
  }

  @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
  @PreAuthorize("hasRole('ROLE_USER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMovie(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable UUID id) {
    movieService.deleteMovie(userDetails.getExternalId(), id);
    return ResponseEntity.ok().build();
  }
}
