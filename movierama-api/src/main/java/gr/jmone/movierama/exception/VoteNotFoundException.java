package gr.jmone.movierama.exception;

public class VoteNotFoundException extends RuntimeException {
  public VoteNotFoundException(String message) {
    super(message);
  }
}
