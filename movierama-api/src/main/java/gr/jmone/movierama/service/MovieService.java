package gr.jmone.movierama.service;

import static gr.jmone.movierama.repository.spec.MovieSpecifications.fromQuery;

import gr.jmone.movierama.dto.MovieDto;
import gr.jmone.movierama.dto.MovieDtoBase;
import gr.jmone.movierama.dto.MovieQueryDto;
import gr.jmone.movierama.mapper.MovieMapper;
import gr.jmone.movierama.repository.MovieRepository;
import gr.jmone.movierama.utils.SecurityUtils;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepository movieRepository;
  private final UserService userService;
  private final MovieMapper movieMapper;

  @Transactional
  public List<MovieDto> getMovies(MovieQueryDto movieQueryDto) {
    return movieRepository.findAll(fromQuery(movieQueryDto)).stream()
        .map(movieMapper::movieToMovieDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public MovieDto saveMovie(String userAuth, MovieDtoBase movieDtoBase) {
    var userExternalId = SecurityUtils.convertStringToUuid(userAuth);
    var movie = movieRepository.findByTitle(movieDtoBase.getTitle());
    if (movie != null) {
      throw new IllegalArgumentException("Movie '" + movieDtoBase.getTitle() + "' already exists!");
    }
    movie = movieMapper.movieDtoBaseToMovie(movieDtoBase);
    movie.setPublisher(userService.findByExternalId(userExternalId));
    return movieMapper.movieToMovieDto(movieRepository.save(movie));
  }

  @Transactional
  public void deleteMovie(String userAuth, UUID movieId) {
    var userExternalId = SecurityUtils.convertStringToUuid(userAuth);
    var movie = movieRepository.findByExternalIdAndPublisher_ExternalId(movieId, userExternalId);
    movie.ifPresentOrElse(
        movieRepository::delete,
        () -> {
          throw new IllegalArgumentException(
              "Movie '" + movieId + "' was not found for user '" + userExternalId + "!");
        });
  }
}
