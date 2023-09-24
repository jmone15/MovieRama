package gr.jmone.movierama.controller.v1;

import static gr.jmone.movierama.config.WebSecurityConfig.BEARER_KEY_SECURITY_SCHEME;

import gr.jmone.movierama.dto.VoteDto;
import gr.jmone.movierama.dto.VoteResponseDto;
import gr.jmone.movierama.security.UserDetailsImpl;
import gr.jmone.movierama.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
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
}
