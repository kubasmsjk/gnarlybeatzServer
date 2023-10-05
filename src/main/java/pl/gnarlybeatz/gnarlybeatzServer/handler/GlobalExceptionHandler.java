package pl.gnarlybeatz.gnarlybeatzServer.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.gnarlybeatz.gnarlybeatzServer.exceptions.ObjectNotValidException;
import pl.gnarlybeatz.gnarlybeatzServer.exceptions.UserExistException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(ObjectNotValidException exception) {

        return ResponseEntity
                .badRequest()
                .body(exception.getErrorMessages());
    }

    @ExceptionHandler(UserExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleException(UserExistException exception) {

        return ResponseEntity
                .badRequest()
                .body(exception.getErrorMessages());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleException(UsernameNotFoundException exception) {

        return ResponseEntity
                .badRequest()
                .body(exception.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleException(AuthenticationException exception) {
        return ResponseEntity
                .badRequest()
                .body(exception.getMessage());
    }
}
