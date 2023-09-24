package gr.jmone.movierama.dto;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MovieDto extends MovieDtoBase {
  private UUID externalId;

  private UserDto publisher;

  private ZonedDateTime publicationDate;

  private Long likes;

  private Long hates;
}
