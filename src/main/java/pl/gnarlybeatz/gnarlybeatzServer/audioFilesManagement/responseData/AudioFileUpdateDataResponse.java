package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.responseData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioFileUpdateDataResponse {
    private String name;
    private String genre;
    private String mood;
    private String bpm;
    private String key;
}
