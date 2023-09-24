package gr.jmone.movierama.service;

import static gr.jmone.movierama.helper.MockData.createMockedVoteDto;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gr.jmone.movierama.exception.MovieRamaProcessingException;
import gr.jmone.movierama.model.Movie;
import gr.jmone.movierama.model.User;
import gr.jmone.movierama.repository.MovieRepository;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

  @Mock private MovieRepository movieRepository;
  @Mock private UserService userService;

  @InjectMocks private VoteService voteService;

  private static final String AUTH_UUID = "c47ce578-5b10-11ee-8c99-0242ac120002";
  private static final String TEST_TITLE = "Test title";
  private static final String TEST_USER = "Test user";

  @Test
  void addVote_initialSubmission_isLike() {
    var movie = mock(Movie.class);
    var user = mock(User.class);
    var movieUuid = UUID.randomUUID();
    when(movieRepository.findByExternalId(any(UUID.class))).thenReturn(movie);
    when(userService.findByExternalId(any(UUID.class))).thenReturn(user);
    when(movieRepository.save(movie)).thenReturn(movie);
    when(movie.getTitle()).thenReturn(TEST_TITLE);
    when(user.getName()).thenReturn(TEST_USER);

    var result = voteService.addVote(AUTH_UUID, createMockedVoteDto(movieUuid, true));

    assertThat(result).isNotNull();
    assertThat(result.getResult())
        .isEqualTo("A positive vote to Test title was added by Test user");
  }

  @Test
  void addVote_initialSubmission_isHate() {
    var movie = mock(Movie.class);
    var user = mock(User.class);
    var movieUuid = UUID.randomUUID();
    when(movieRepository.findByExternalId(any(UUID.class))).thenReturn(movie);
    when(userService.findByExternalId(any(UUID.class))).thenReturn(user);
    when(movieRepository.save(movie)).thenReturn(movie);
    when(movie.getTitle()).thenReturn(TEST_TITLE);
    when(user.getName()).thenReturn(TEST_USER);

    var result = voteService.addVote(AUTH_UUID, createMockedVoteDto(movieUuid, false));

    assertThat(result).isNotNull();
    assertThat(result.getResult())
        .isEqualTo("A negative vote to Test title was added by Test user");
  }

  @Test
  void addVote_invalidUser_throwError() {
    var movie = mock(Movie.class);
    var movieUuid = UUID.randomUUID();
    when(movieRepository.findByExternalId(any(UUID.class))).thenReturn(movie);
    when(userService.findByExternalId(any(UUID.class))).thenReturn(null);

    assertThatThrownBy(() -> voteService.addVote(AUTH_UUID, createMockedVoteDto(movieUuid, true)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("User with ID '" + AUTH_UUID + "' is not valid!");
  }

  @Test
  void addVote_movieNotFound_throwError() {
    var movieUuid = UUID.randomUUID();
    when(movieRepository.findByExternalId(any(UUID.class))).thenReturn(null);

    assertThatThrownBy(() -> voteService.addVote(AUTH_UUID, createMockedVoteDto(movieUuid, true)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Movie with ID '" + movieUuid + "' does not exists!");
  }

  @Test
  void addVote_userIsPublisher_throwError() {
    var movie = mock(Movie.class);
    var user = mock(User.class);
    var movieUuid = UUID.randomUUID();
    when(movieRepository.findByExternalId(any(UUID.class))).thenReturn(movie);
    when(userService.findByExternalId(any(UUID.class))).thenReturn(user);
    when(movie.getTitle()).thenReturn(TEST_TITLE);
    when(user.getName()).thenReturn(TEST_USER);
    when(movie.getPublisher()).thenReturn(user);

    assertThatThrownBy(() -> voteService.addVote(AUTH_UUID, createMockedVoteDto(movieUuid, true)))
        .isInstanceOf(MovieRamaProcessingException.class)
        .hasMessage("Test user is the publisher of 'Test title'!");
  }

  @Test
  void addVote_userChangedVote_toHate_returnResult() {
    var movie = mock(Movie.class);
    var user = mock(User.class);
    var likes = mock(Set.class);
    var movieUuid = UUID.randomUUID();
    when(movieRepository.findByExternalId(any(UUID.class))).thenReturn(movie);
    when(userService.findByExternalId(any(UUID.class))).thenReturn(user);
    when(movie.getTitle()).thenReturn(TEST_TITLE);
    when(user.getName()).thenReturn(TEST_USER);
    when(movie.getLikes()).thenReturn(likes);
    when(likes.contains(any(User.class))).thenReturn(true);

    var result = voteService.addVote(AUTH_UUID, createMockedVoteDto(movieUuid, false));
    assertThat(result).isNotNull();
    assertThat(result.getResult()).isEqualTo("Test user changed the Test title vote to a hate!");
  }

  @Test
  void addVote_userChangedVote_toLike_returnResult() {
    var movie = mock(Movie.class);
    var user = mock(User.class);
    var hates = mock(Set.class);
    var movieUuid = UUID.randomUUID();
    when(movieRepository.findByExternalId(any(UUID.class))).thenReturn(movie);
    when(userService.findByExternalId(any(UUID.class))).thenReturn(user);
    when(movie.getTitle()).thenReturn(TEST_TITLE);
    when(user.getName()).thenReturn(TEST_USER);
    when(movie.getHates()).thenReturn(hates);
    when(hates.contains(any(User.class))).thenReturn(true);

    var result = voteService.addVote(AUTH_UUID, createMockedVoteDto(movieUuid, true));
    assertThat(result).isNotNull();
    assertThat(result.getResult()).isEqualTo("Test user changed the Test title vote to a like!");
  }

  @Test
  void addVote_userRetractedVote_fromLike_returnResult() {
    var movie = mock(Movie.class);
    var user = mock(User.class);
    var likes = mock(Set.class);
    var movieUuid = UUID.randomUUID();
    when(movieRepository.findByExternalId(any(UUID.class))).thenReturn(movie);
    when(userService.findByExternalId(any(UUID.class))).thenReturn(user);
    when(movie.getTitle()).thenReturn(TEST_TITLE);
    when(user.getName()).thenReturn(TEST_USER);
    when(movie.getLikes()).thenReturn(likes);
    when(likes.contains(any(User.class))).thenReturn(true);

    var result = voteService.addVote(AUTH_UUID, createMockedVoteDto(movieUuid, true));
    assertThat(result).isNotNull();
    assertThat(result.getResult())
        .isEqualTo("A positive vote to Test title was retracted by Test user");
  }

  @Test
  void addVote_userRetractedVote_fromHate_returnResult() {
    var movie = mock(Movie.class);
    var user = mock(User.class);
    var hates = mock(Set.class);
    var movieUuid = UUID.randomUUID();
    when(movieRepository.findByExternalId(any(UUID.class))).thenReturn(movie);
    when(userService.findByExternalId(any(UUID.class))).thenReturn(user);
    when(movie.getTitle()).thenReturn(TEST_TITLE);
    when(user.getName()).thenReturn(TEST_USER);
    when(movie.getHates()).thenReturn(hates);
    when(hates.contains(any(User.class))).thenReturn(true);

    var result = voteService.addVote(AUTH_UUID, createMockedVoteDto(movieUuid, false));
    assertThat(result).isNotNull();
    assertThat(result.getResult())
        .isEqualTo("A negative vote to Test title was retracted by Test user");
  }
}
