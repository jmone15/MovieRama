package gr.jmone.movierama.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class MovieDtoBase {
  @NotEmpty private String title;

  @Size(max = 250)
  private String description;
}
