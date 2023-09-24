package gr.jmone.movierama.controller.v1;

import static gr.jmone.movierama.config.WebSecurityConfig.BEARER_KEY_SECURITY_SCHEME;

import gr.jmone.movierama.dto.VoteDto;
import gr.jmone.movierama.dto.VoteResponseDto;
import gr.jmone.movierama.security.UserDetailsImpl;
import gr.jmone.movierama.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/votes")
public class VoteController {

  private final VoteService voteService;

  @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
  @PreAuthorize("hasRole('ROLE_USER')")
  @PutMapping
  public ResponseEntity<VoteResponseDto> vote(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody VoteDto voteDto) {
    return ResponseEntity.ok().body(voteService.addVote(userDetails.getExternalId(), voteDto));
  }

  @Operation(security = {@SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME)})
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping
  public ResponseEntity<VoteDto> list(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam UUID movieId) {
    return ResponseEntity.ok().body(voteService.listVote(userDetails.getExternalId(), movieId));
  }
}
