package gr.jmone.movierama.utils;

import gr.jmone.movierama.exception.MovieRamaProcessingException;
import java.util.UUID;

public class SecurityUtils {

  private SecurityUtils() {}

  public static UUID convertStringToUuid(String userAuth) {
    UUID accessToken;
    try {
      accessToken = UUID.fromString(userAuth);
    } catch (Exception e) {
      throw new MovieRamaProcessingException("Invalid HTTP user token provided: " + userAuth);
    }
    return accessToken;
  }
}
