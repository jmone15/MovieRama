package gr.jmone.movierama.dto;

import gr.jmone.movierama.repository.spec.OrderTerm;
import gr.jmone.movierama.repository.spec.SortingDirection;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MovieQueryDto {
  private UUID userId;
  private OrderTerm term = OrderTerm.PUBLICATION_DATE;
  private SortingDirection direction = SortingDirection.DESC;
}
