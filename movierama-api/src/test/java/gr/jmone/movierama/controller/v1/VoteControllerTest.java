package gr.jmone.movierama.controller.v1;

import static gr.jmone.movierama.helper.MockData.createMockedVoteDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.jmone.movierama.dto.VoteDto;
import gr.jmone.movierama.dto.VoteResponseDto;
import gr.jmone.movierama.security.UserDetailsImpl;
import gr.jmone.movierama.service.VoteService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VoteController.class)
@AutoConfigureMockMvc(addFilters = false)
class VoteControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private VoteService voteService;

  @MockBean private UserDetailsImpl userDetails;

  @BeforeEach
  void setupAuthentication() {
    when(userDetails.getExternalId()).thenReturn(UUID.randomUUID().toString());
    var context = SecurityContextHolder.getContext();
    context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, null));
  }

  @Test
  @WithMockUser(authorities = "ROLE_USER")
  void vote_withValidPayload_thenReturn() throws Exception {
    var expectedText = "A positive vote to Gone with the wind was added by Test User";
    var voteDto = createMockedVoteDto(UUID.randomUUID(), true);
    var voteResponseDto = VoteResponseDto.builder().result(expectedText).build();
    when(voteService.addVote(anyString(), any(VoteDto.class))).thenReturn(voteResponseDto);

    mvc.perform(
            put("/api/v1/votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voteDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result").value(expectedText))
        .andReturn();
  }
}
