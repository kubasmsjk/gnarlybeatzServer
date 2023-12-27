package pl.gnarlybeatz.gnarlybeatzServer.exceptions;

import lombok.Data;

import java.io.IOException;

@Data
public class FileUploadException extends IOException {
    private final String errorMessages;
}
