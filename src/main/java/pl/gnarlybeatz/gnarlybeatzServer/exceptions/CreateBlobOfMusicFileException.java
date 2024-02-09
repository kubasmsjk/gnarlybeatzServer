package pl.gnarlybeatz.gnarlybeatzServer.exceptions;

import lombok.Data;

@Data
public class CreateBlobOfMusicFileException extends RuntimeException {
    private final String errorMessages;
}
