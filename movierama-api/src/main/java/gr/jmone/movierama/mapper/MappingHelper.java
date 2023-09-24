package gr.jmone.movierama.mapper;

import gr.jmone.movierama.model.User;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MappingHelper {

  @Named("likesToLikesCount")
  public Long likesToLikesCount(Set<User> likes) {
    if (likes != null) {
      return (long) likes.size();
    }
    return 0L;
  }

  @Named("hatesToHatesCount")
  public Long hatesToHatesCount(Set<User> hates) {
    if (hates != null) {
      return (long) hates.size();
    }
    return 0L;
  }
}
