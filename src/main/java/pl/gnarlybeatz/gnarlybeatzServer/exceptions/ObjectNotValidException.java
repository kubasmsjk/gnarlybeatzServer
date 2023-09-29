package pl.gnarlybeatz.gnarlybeatzServer.exceptions;

import jakarta.validation.Path;
import lombok.Data;

import java.util.Map;

@Data
public class ObjectNotValidException extends RuntimeException {

    private final Map<Path, String> errorMessages;

}
