package gr.jmone.movierama.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.jmone.movierama.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static gr.jmone.movierama.helper.MockData.createMockedMovieBaseDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(authorities = "ROLE_USER")
class MovieControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private MovieService movieService;

  @Test
  void createMovie_givenInvalidPayload_thenReturnErrorResponse() throws Exception {

    var movieBaseDto = createMockedMovieBaseDto("", "Test description");

    mvc.perform(
            post("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieBaseDto)))
        .andExpect(status().isBadRequest())
        .andReturn();
  }
}
