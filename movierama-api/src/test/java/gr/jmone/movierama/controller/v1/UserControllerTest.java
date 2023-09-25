package gr.jmone.movierama.controller.v1;

import static gr.jmone.movierama.helper.MockData.createMockedUserDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gr.jmone.movierama.service.UserService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

  @Autowired private MockMvc mvc;

  @MockBean private UserService userService;

  @Test
  @WithMockUser(authorities = "ROLE_ADMIN")
  void getAllUser_fullyAuthenticated_thenReturnList() throws Exception {
    var userDto = createMockedUserDto();
    when(userService.getAllUsers()).thenReturn(List.of(userDto));

    mvc.perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].externalId").value(userDto.getExternalId().toString()))
        .andReturn();
  }

  @Test
  @WithMockUser(authorities = "ROLE_ADMIN")
  void getUser_withValidId_thenReturn() throws Exception {
    var userDto = createMockedUserDto();
    when(userService.validateAndGetUserByExternalId(any(UUID.class))).thenReturn(userDto);

    mvc.perform(get("/api/v1/users/" + UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.externalId").value(userDto.getExternalId().toString()))
        .andReturn();
  }

  @Test
  void deleteUser_withValidId_thenReturn() throws Exception {
    var userDto = createMockedUserDto();
    when(userService.validateAndGetUserByExternalId(any(UUID.class))).thenReturn(userDto);

    mvc.perform(
            delete("/api/v1/users/" + UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }
}
