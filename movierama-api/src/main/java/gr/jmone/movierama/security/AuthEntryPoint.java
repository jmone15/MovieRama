package gr.jmone.movierama.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import gr.jmone.movierama.dto.AuthErrorResponseDto;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

  @SneakyThrows
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) {

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    final var body =
        AuthErrorResponseDto.builder()
            .status(HttpServletResponse.SC_UNAUTHORIZED)
            .error("Unauthorized")
            .message(authException.getMessage())
            .build();

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);
  }
}
