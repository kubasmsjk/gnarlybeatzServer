package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadAudioFileRequest {
    private MultipartFile audioFile;
    private MultipartFile image;
    private String genre;
    private String mood;
    private String bpm;
    private String key;
}
