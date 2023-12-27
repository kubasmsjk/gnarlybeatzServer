package pl.gnarlybeatz.gnarlybeatzServer.exceptions;

import lombok.Data;

import java.util.Map;

@Data
public class CreateBlobOfMusicFileException extends RuntimeException{
    private final String errorMessages;
}
