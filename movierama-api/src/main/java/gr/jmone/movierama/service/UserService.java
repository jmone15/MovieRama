package gr.jmone.movierama.service;

import gr.jmone.movierama.dto.UserDto;
import gr.jmone.movierama.exception.MovieRamaProcessingException;
import gr.jmone.movierama.exception.MovieRamaUserNotFoundException;
import gr.jmone.movierama.mapper.UserMapper;
import gr.jmone.movierama.model.User;
import gr.jmone.movierama.repository.MovieRepository;
import gr.jmone.movierama.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final MovieRepository movieRepository;
  private final UserMapper userMapper;

  @Transactional
  public UserDto validateAndGetUserByExternalId(UUID id) {
    var user = findByExternalId(id);
    return userMapper.userToUserDto(user);
  }

  @Transactional
  public List<UserDto> getAllUsers() {
    return userRepository.findAll().stream()
        .map(userMapper::userToUserDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public User findByExternalId(UUID externalId) {
    return userRepository
        .findByExternalId(externalId)
        .orElseThrow(
            () ->
                new MovieRamaUserNotFoundException(
                    String.format("User with id %s not found", externalId)));
  }

  @Transactional
  public void deleteUser(UUID externalId) {
    var user =
        userRepository
            .findByExternalId(externalId)
            .orElseThrow(
                () ->
                    new MovieRamaUserNotFoundException(
                        String.format("User with id %s not found", externalId)));
    var movies = movieRepository.findByRelation(user.getId());
    if (!movies.isEmpty()) {
      throw new MovieRamaProcessingException(
          String.format(
              "User with id %s has active votes, please retract them before deleting", externalId));
    }
    userRepository.delete(user);
  }
}
