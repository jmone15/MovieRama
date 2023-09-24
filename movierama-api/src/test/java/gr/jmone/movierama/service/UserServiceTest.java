package gr.jmone.movierama.service;

import static gr.jmone.movierama.helper.MockData.createMockedUserDto;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import gr.jmone.movierama.exception.MovieRamaProcessingException;
import gr.jmone.movierama.exception.MovieRamaUserNotFoundException;
import gr.jmone.movierama.mapper.UserMapper;
import gr.jmone.movierama.model.Movie;
import gr.jmone.movierama.model.User;
import gr.jmone.movierama.repository.MovieRepository;
import gr.jmone.movierama.repository.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private MovieRepository movieRepository;
  @Mock private UserMapper userMapper;
  @InjectMocks private UserService userService;

  @Test
  void givenUUID_validateAndGetUser() {
    var userDto = createMockedUserDto();
    when(userRepository.findByExternalId(any(UUID.class)))
        .thenReturn(Optional.ofNullable(mock(User.class)));
    when(userMapper.userToUserDto(any(User.class))).thenReturn(userDto);

    assertThat(userService.validateAndGetUserByExternalId(UUID.randomUUID())).isNotNull();
  }

  @Test
  void givenUUID_validateAndGetUser_throwError() {
    var uuid = UUID.randomUUID();
    when(userRepository.findByExternalId(any(UUID.class))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.validateAndGetUserByExternalId(uuid))
        .isInstanceOf(MovieRamaUserNotFoundException.class)
        .hasMessage("User with id " + uuid + " not found");
  }

  @Test
  void getAllUsers_getList() {
    var userDto = createMockedUserDto();
    when(userRepository.findAll()).thenReturn(List.of(mock(User.class)));
    when(userMapper.userToUserDto(any(User.class))).thenReturn(userDto);

    assertThat(userService.getAllUsers().size()).isEqualTo(1);
  }

  @Test
  void deleteUser_userNotFound_throwError() {
    var uuid = UUID.randomUUID();
    when(userRepository.findByExternalId(any(UUID.class))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.deleteUser(uuid))
        .isInstanceOf(MovieRamaUserNotFoundException.class)
        .hasMessage("User with id " + uuid + " not found");
  }

  @Test
  void deleteUser_userHasActiveVotes_throwError() {
    var uuid = UUID.randomUUID();
    when(userRepository.findByExternalId(any(UUID.class)))
        .thenReturn(Optional.ofNullable(mock(User.class)));
    when(movieRepository.findByRelation(any(Long.class))).thenReturn(List.of(mock(Movie.class)));

    assertThatThrownBy(() -> userService.deleteUser(uuid))
        .isInstanceOf(MovieRamaProcessingException.class)
        .hasMessage(
            "User with id " + uuid + " has active votes, please retract them before deleting");
  }

  @Test
  void deleteUser_proceedWithDeletion() {
    var uuid = UUID.randomUUID();
    when(userRepository.findByExternalId(any(UUID.class)))
        .thenReturn(Optional.ofNullable(mock(User.class)));
    when(movieRepository.findByRelation(any(Long.class))).thenReturn(Collections.emptyList());
    doNothing().when(userRepository).delete(any(User.class));
    userService.deleteUser(uuid);

    verify(userRepository).delete(any(User.class));
  }
}
