package gr.jmone.movierama.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequestDto {
  @Schema(example = "username")
  @NotBlank
  private String username;

  @Schema(example = "password")
  @NotBlank
  private String password;

  @Schema(example = "User")
  @NotBlank
  private String name;

  @Schema(example = "user@mycompany.com")
  @Email
  private String email;
}
