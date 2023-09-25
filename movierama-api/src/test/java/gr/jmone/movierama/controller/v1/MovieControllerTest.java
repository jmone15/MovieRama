package gr.jmone.movierama.controller.v1;

import static gr.jmone.movierama.helper.MockData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.jmone.movierama.dto.MovieDtoBase;
import gr.jmone.movierama.dto.MovieQueryDto;
import gr.jmone.movierama.security.UserDetailsImpl;
import gr.jmone.movierama.service.MovieService;
import java.util.List;
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

@WebMvcTest(MovieController.class)
@AutoConfigureMockMvc(addFilters = false)
class MovieControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private MovieService movieService;

  @MockBean private UserDetailsImpl userDetails;

  @BeforeEach
  void setupAuthentication() {
    when(userDetails.getExternalId()).thenReturn(UUID.randomUUID().toString());
    var context = SecurityContextHolder.getContext();
    context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, null));
  }

  @Test
  @WithMockUser(authorities = "ROLE_USER")
  void createMovie_givenValidPayload_thenReturnResponse() throws Exception {
    var movieBaseDto = createMockedMovieBaseDto("Title", "Test description");
    var userDto = createMockedUserDto();
    var movieDto = createMockedMovieDto("Title", "Description", userDto);

    when(movieService.saveMovie(anyString(), any(MovieDtoBase.class))).thenReturn(movieDto);
    mvc.perform(
            post("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieBaseDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value(movieDto.getTitle()))
        .andExpect(jsonPath("$.description").value(movieDto.getDescription()))
        .andExpect(jsonPath("$.externalId").value(movieDto.getExternalId().toString()))
        .andExpect(
            jsonPath("$.publisher.externalId")
                .value(movieDto.getPublisher().getExternalId().toString()))
        .andReturn();
  }

  @Test
  @WithMockUser(authorities = "ROLE_USER")
  void createMovie_givenInvalidPayload_thenReturnErrorResponse() throws Exception {

    var movieBaseDto = createMockedMovieBaseDto("", "Test description");

    mvc.perform(
            post("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieBaseDto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorMessage").value("must not be empty"))
        .andReturn();
  }

  @Test
  void getMovies_givenValidParams_thenReturn() throws Exception {
    var movieUuid = UUID.randomUUID();
    var userDto = createMockedUserDto();
    var movieDto = createMockedMovieDto("Title", "Test description", userDto);
    when(movieService.getMovies(any(MovieQueryDto.class))).thenReturn(List.of(movieDto));

    mvc.perform(
            get("/api/v1/movies?userId=" + movieUuid + "&term=PUBLICATION_DATE&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].title").value(movieDto.getTitle()))
        .andExpect(jsonPath("$.[0].description").value(movieDto.getDescription()))
        .andExpect(jsonPath("$.[0].externalId").value(movieDto.getExternalId().toString()))
        .andReturn();
  }

  @Test
  @WithMockUser(authorities = "ROLE_USER")
  void deleteMovie_givenValidParams_thenReturn() throws Exception {
    var movieUuid = UUID.randomUUID();
    doNothing().when(movieService).deleteMovie(anyString(), any(UUID.class));

    mvc.perform(delete("/api/v1/movies/" + movieUuid).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }
}
