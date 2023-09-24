package gr.jmone.movierama.exception;

public class MovieRamaUserNotFoundException extends RuntimeException {
  public MovieRamaUserNotFoundException(String message) {
    super(message);
  }
}
