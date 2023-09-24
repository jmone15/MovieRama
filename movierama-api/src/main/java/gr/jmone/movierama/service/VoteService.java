package gr.jmone.movierama.service;

import gr.jmone.movierama.dto.VoteDto;
import gr.jmone.movierama.dto.VoteResponseDto;
import gr.jmone.movierama.exception.MovieRamaProcessingException;
import gr.jmone.movierama.repository.MovieRepository;
import gr.jmone.movierama.utils.SecurityUtils;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoteService {
  private final MovieRepository movieRepository;
  private final UserService userService;

  @Transactional
  public VoteResponseDto addVote(String userAuth, VoteDto voteDto) {
    String result;

    var userExternalId = SecurityUtils.convertStringToUuid(userAuth);
    var movie = movieRepository.findByExternalId(voteDto.getMovie());
    var user = userService.findByExternalId(userExternalId);

    if (movie == null) {
      throw new IllegalArgumentException(
          "Movie with ID '" + voteDto.getMovie() + "' does not exists!");
    }
    if (user == null) {
      throw new IllegalArgumentException("User with ID '" + userExternalId + "' is not valid!");
    } else {
      var publisher = movie.getPublisher();
      var likes = movie.getLikes();
      var hates = movie.getHates();

      if (user.equals(publisher)) {
        throw new MovieRamaProcessingException(
            publisher.getName() + " is the publisher of '" + movie.getTitle() + "'!");
      } else if (likes.contains(user) && voteDto.isLike()) {
        result =
            String.format(
                "A positive vote to %s was retracted by %s", movie.getTitle(), user.getName());
        movie.removeLike(user);
      } else if (hates.contains(user) && !voteDto.isLike()) {
        result =
            String.format(
                "A negative vote to %s was retracted by %s", movie.getTitle(), user.getName());
        movie.removeHate(user);
      } else if (likes.contains(user) && !voteDto.isLike()) {
        movie.removeLike(user);
        movie.addHate(user);
        result =
            String.format("%s changed the %s vote to a hate!", user.getName(), movie.getTitle());
      } else if (hates.contains(user) && voteDto.isLike()) {
        movie.removeHate(user);
        movie.addLike(user);
        result =
            String.format("%s changed the %s vote to a like!", user.getName(), movie.getTitle());
      } else {
        String vote;
        if (voteDto.isLike()) {
          vote = "positive vote";
          movie.addLike(user);
        } else {
          vote = "negative vote";
          movie.addHate(user);
        }
        movieRepository.save(movie);
        result =
            String.format("A %s to %s was added by %s", vote, movie.getTitle(), user.getName());
      }

      log.info(result);

      return VoteResponseDto.builder().result(result).build();
    }
  }
}
