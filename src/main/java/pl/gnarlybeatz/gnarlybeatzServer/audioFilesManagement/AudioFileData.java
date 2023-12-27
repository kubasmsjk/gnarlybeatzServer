package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioFileData {

    private String name;
    private String genre;
    private String mood;
    private String bpm;
    private String key;
    private byte[] audioBlob;
    private byte[] imageBlob;
}
