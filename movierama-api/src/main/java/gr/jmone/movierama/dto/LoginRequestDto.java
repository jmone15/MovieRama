package gr.jmone.movierama.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
  @Schema(example = "user")
  @NotBlank
  private String username;

  @Schema(example = "password")
  @NotBlank
  private String password;
}
