package gr.jmone.movierama.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MovieDtoBase {
  @NotEmpty private String title;
  private String description;
}
