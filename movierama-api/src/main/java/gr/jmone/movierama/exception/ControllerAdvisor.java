package gr.jmone.movierama.exception;

import gr.jmone.movierama.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MovieRamaProcessingException.class)
    public ResponseEntity<ErrorResponseDto> handleMovieRamaExceptions(
            MovieRamaProcessingException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.name(), ex.getMessage()));
    }

    @ExceptionHandler(MovieRamaUserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundExceptions(
            MovieRamaProcessingException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body((new ErrorResponseDto(HttpStatus.NOT_FOUND.name(), ex.getMessage())));
    }

    @ExceptionHandler(VoteNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleVoteNotFoundExceptions(VoteNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(HttpStatus.NOT_FOUND.name(), ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUsernameNotFoundException(
            UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(HttpStatus.FORBIDDEN.name(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericExceptions(Exception ex) {
        var message =
                ex.getMessage() != null
                        ? ex.getMessage()
                        : HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        return ResponseEntity.internalServerError()
                .body(new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.name(), message));
    }
}
