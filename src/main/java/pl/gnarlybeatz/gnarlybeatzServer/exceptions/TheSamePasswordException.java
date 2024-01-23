package pl.gnarlybeatz.gnarlybeatzServer.exceptions;

import lombok.Data;

import java.util.Map;

@Data
public class TheSamePasswordException extends RuntimeException {
    private final Map<String, String> errorMessages;
}
