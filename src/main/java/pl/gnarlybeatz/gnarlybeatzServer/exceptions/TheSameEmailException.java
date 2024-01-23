package pl.gnarlybeatz.gnarlybeatzServer.exceptions;

import lombok.Data;

import java.util.Map;

@Data
public class TheSameEmailException extends RuntimeException{
    private final Map<String, String> errorMessages;
}
