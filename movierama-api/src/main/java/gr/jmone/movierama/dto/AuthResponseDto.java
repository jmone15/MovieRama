package gr.jmone.movierama.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {
  private String token;
}
