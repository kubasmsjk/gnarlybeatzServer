package pl.gnarlybeatz.gnarlybeatzServer.exceptions;

import lombok.Data;

import java.util.Map;

@Data
public class UserExistException extends RuntimeException {

    private final Map<String, Boolean> errorMessages;

}