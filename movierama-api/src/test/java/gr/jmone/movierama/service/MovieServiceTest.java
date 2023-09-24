package gr.jmone.movierama.service;

import static gr.jmone.movierama.helper.MockData.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import gr.jmone.movierama.dto.MovieDtoBase;
import gr.jmone.movierama.mapper.MovieMapper;
import gr.jmone.movierama.model.Movie;
import gr.jmone.movierama.model.User;
import gr.jmone.movierama.repository.MovieRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

  @Mock private MovieRepository movieRepository;
  @Mock private MovieMapper movieMapper;
  @Mock private UserService userService;
  @InjectMocks private MovieService movieService;

  private static final String AUTH_UUID = "c47ce578-5b10-11ee-8c99-0242ac120002";
  private static final String TEST_TITLE = "Test title";
  private static final String TEST_DESCRIPTION = "Test description";

  @Test
  void givenValid_userAuthAndMovieBaseDto_saveMovieAndReturn() {
    var movie = mock(Movie.class);
    var user = mock(User.class);
    var movieBaseDto = createMockedMovieBaseDto(TEST_TITLE, TEST_DESCRIPTION);
    var userDto = createMockedUserDto();
    var movieDto = createMockedMovieDto(TEST_TITLE, TEST_DESCRIPTION, userDto);

    when(movieRepository.findByTitle(anyString())).thenReturn(null);
    when(movieMapper.movieDtoBaseToMovie(any(MovieDtoBase.class))).thenReturn(movie);
    when(userService.findByExternalId(any(UUID.class))).thenReturn(user);
    when(movieRepository.save(any(Movie.class))).thenReturn(movie);
    when(movieMapper.movieToMovieDto(any(Movie.class))).thenReturn(movieDto);

    var result = movieService.saveMovie(AUTH_UUID, movieBaseDto);

    assertThat(result).isNotNull();
  }

  @Test
  void givenValid_userAuthAndMovieBaseDto_movieExists_throwError() {
    var movie = mock(Movie.class);
    var movieBaseDto = createMockedMovieBaseDto(TEST_TITLE, TEST_DESCRIPTION);
    when(movieRepository.findByTitle(anyString())).thenReturn(movie);

    assertThatThrownBy(() -> movieService.saveMovie(AUTH_UUID, movieBaseDto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Movie 'Test title' already exists!");
  }

  @Test
  void getMovies_hasEntries_return() {
    var movie = mock(Movie.class);
    var userDto = createMockedUserDto();
    var movieDto = createMockedMovieDto(TEST_TITLE, TEST_DESCRIPTION, userDto);
    var movieQueryDto = createMockedMovieQueryDto();
    when(movieRepository.findAll(any(Specification.class))).thenReturn(List.of(movie));
    when(movieMapper.movieToMovieDto(any(Movie.class))).thenReturn(movieDto);

    var result = movieService.getMovies(movieQueryDto);

    assertThat(result.size()).isEqualTo(1);
  }

  @Test
  void getMovies_notEntries_returnEmptyList() {
    var movieQueryDto = createMockedMovieQueryDto();
    when(movieRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

    var result = movieService.getMovies(movieQueryDto);

    assertThat(result.size()).isEqualTo(0);
  }

  @Test
  void deleteMovie_isFound_doDelete() {
    when(movieRepository.findByExternalIdAndPublisher_ExternalId(any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.ofNullable(mock(Movie.class)));
    doNothing().when(movieRepository).delete(any(Movie.class));

    movieService.deleteMovie(AUTH_UUID, UUID.randomUUID());

    verify(movieRepository).delete(any(Movie.class));
  }

  @Test
  void deleteMovie_notPresent_doThrowError() {
    var uuid = UUID.randomUUID();
    when(movieRepository.findByExternalIdAndPublisher_ExternalId(any(UUID.class), any(UUID.class)))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> movieService.deleteMovie(AUTH_UUID, uuid))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Movie '" + uuid + "' was not found for user '" + AUTH_UUID + "!");
  }
}
