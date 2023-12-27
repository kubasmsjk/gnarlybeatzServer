package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDataRequest {
    private String name;
    private String genre;
    private String mood;
    private String bpm;
    private String key;
}
