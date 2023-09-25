package gr.jmone.movierama.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class MovieDtoBase {
  @NotEmpty private String title;

  @Size(max = 250)
  private String description;
}
