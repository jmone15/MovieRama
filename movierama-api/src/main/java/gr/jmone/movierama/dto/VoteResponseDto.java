package gr.jmone.movierama.dto;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoteResponseDto {
  @NotNull private String result;
}
