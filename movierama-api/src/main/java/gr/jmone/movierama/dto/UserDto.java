package gr.jmone.movierama.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class UserDto {
  private Long id;

  private String username;

  private String password;

  private UUID externalId;

  private String name;

  private String email;

  private String role;
}
