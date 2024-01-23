package pl.gnarlybeatz.gnarlybeatzServer.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.gnarlybeatz.gnarlybeatzServer.exceptions.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotValidException.class)
    public ResponseEntity<?> handleException(ObjectNotValidException exception) {

        return ResponseEntity
                .badRequest()
                .body(exception.getErrorMessages());
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<?> handleException(UserExistException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getErrorMessages());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleException(UsernameNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("account", exception.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleException(AuthenticationException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("account", "Couldn't find your account."));
    }

    @ExceptionHandler(TheSamePasswordException.class)
    public ResponseEntity<?> handleException(TheSamePasswordException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getErrorMessages());
    }

    @ExceptionHandler(TheSameEmailException.class)
    public ResponseEntity<?> handleException(TheSameEmailException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getErrorMessages());
    }

    @ExceptionHandler(FileNotExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleException(FileNotExistException exception) {

        return ResponseEntity
                .badRequest()
                .body(exception.getErrorMessages());
    }

}
