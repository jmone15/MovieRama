package gr.jmone.movierama.controller;

import static gr.jmone.movierama.helper.MockData.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.jmone.movierama.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private AuthenticationService authenticationService;

  @Test
  void login_withValidPayload_thenReturnResponse() throws Exception {
    var loginRequest = createLoginRequestDto();
    var authResponse = createAuthResponseDto();
    when(authenticationService.handleUserLogin(loginRequest)).thenReturn(authResponse);

    mvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value(authResponse.getToken()))
        .andReturn();
  }

  @Test
  void signup_withValidPayload_thenReturnResponse() throws Exception {
    var signup = createSignupRequestDto();
    var authResponse = createAuthResponseDto();
    when(authenticationService.handleUserRegistration(signup)).thenReturn(authResponse);

    mvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signup)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.token").value(authResponse.getToken()))
        .andReturn();
  }

  @Test
  void login_withInvalidPayload_thenReturnErrorResponse() throws Exception {
    var loginRequest = createLoginRequestDto();
    loginRequest.setUsername("");

    mvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.errorMessage").value("must not be blank"))
        .andReturn();
  }

  @Test
  void signup_withInvalidPayload_thenReturnErrorResponse() throws Exception {
    var signup = createSignupRequestDto();
    signup.setEmail("invalid");

    mvc.perform(
            post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signup)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorType").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.errorMessage").value("must be a well-formed email address"))
        .andReturn();
  }
}
