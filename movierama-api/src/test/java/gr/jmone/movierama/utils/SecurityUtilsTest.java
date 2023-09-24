package gr.jmone.movierama.utils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import gr.jmone.movierama.exception.MovieRamaProcessingException;
import org.junit.jupiter.api.Test;

class SecurityUtilsTest {

  @Test
  void convertStringToUuid_invalidInput_throwError() {
    assertThatThrownBy(() -> SecurityUtils.convertStringToUuid("invalid"))
        .isInstanceOf(MovieRamaProcessingException.class)
        .hasMessage("Invalid HTTP user token provided: invalid");
  }
}
