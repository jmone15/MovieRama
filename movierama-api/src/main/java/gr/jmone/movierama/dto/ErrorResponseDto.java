package gr.jmone.movierama.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDto {
  private String errorType;
  private String errorMessage;
}
