package gr.jmone.movierama.mapper;

import gr.jmone.movierama.dto.MovieDto;
import gr.jmone.movierama.dto.MovieDtoBase;
import gr.jmone.movierama.model.Movie;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MappingHelper.class)
public interface MovieMapper {
  @Mapping(target = "likes", qualifiedByName = "likesToLikesCount")
  @Mapping(target = "hates", qualifiedByName = "hatesToHatesCount")
  MovieDto movieToMovieDto(Movie movie);

  @Mapping(target = "externalId", expression = "java(this.generateUuid())")
  @Mapping(target = "publicationDate", expression = "java(this.generatePublicationDate())")
  Movie movieDtoBaseToMovie(MovieDtoBase movieDtoBase);

  default ZonedDateTime generatePublicationDate() {
    return ZonedDateTime.now();
  }

  default UUID generateUuid() {
    return UUID.randomUUID();
  }
}
