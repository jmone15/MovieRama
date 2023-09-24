package gr.jmone.movierama.dto;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class VoteDto {
  @NonNull @NotNull private UUID movie;

  @NotNull private boolean like;
}
