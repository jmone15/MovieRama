package gr.jmone.movierama.helper;

import gr.jmone.movierama.dto.*;
import gr.jmone.movierama.repository.spec.OrderTerm;
import gr.jmone.movierama.repository.spec.SortingDirection;
import java.time.ZonedDateTime;
import java.util.UUID;

public class MockData {

  public static MovieDtoBase createMockedMovieBaseDto(String title, String description) {
    var movieBaseDto = new MovieDtoBase();
    movieBaseDto.setTitle(title);
    movieBaseDto.setDescription(description);
    return movieBaseDto;
  }

  public static UserDto createMockedUserDto() {
    var userDto = new UserDto();
    userDto.setExternalId(UUID.randomUUID());

    return userDto;
  }

  public static MovieDto createMockedMovieDto(String title, String description, UserDto userDto) {
    var movieDto = new MovieDto();
    movieDto.setExternalId(UUID.randomUUID());
    movieDto.setPublicationDate(ZonedDateTime.now());
    movieDto.setPublisher(userDto);
    movieDto.setTitle(title);
    movieDto.setDescription(description);

    return movieDto;
  }

  public static MovieQueryDto createMockedMovieQueryDto() {
    var movieQueryDto = new MovieQueryDto();
    movieQueryDto.setUserId(UUID.randomUUID());
    movieQueryDto.setTerm(OrderTerm.PUBLICATION_DATE);
    movieQueryDto.setDirection(SortingDirection.DESC);

    return movieQueryDto;
  }

  public static VoteDto createMockedVoteDto(UUID movieId, boolean like) {
    return new VoteDto(movieId, like);
  }

  public static LoginRequestDto createLoginRequestDto() {
    var loginDto = new LoginRequestDto();
    loginDto.setUsername("user");
    loginDto.setPassword("password");

    return loginDto;
  }

  public static SignUpRequestDto createSignupRequestDto() {
    var signDto = new SignUpRequestDto();
    signDto.setUsername("user");
    signDto.setPassword("password");
    signDto.setName("name");
    signDto.setEmail("email");

    return signDto;
  }
}
