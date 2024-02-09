package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.requestData;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import pl.gnarlybeatz.gnarlybeatzServer.validator.ObjectRequestInterface;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadAudioFileRequest implements ObjectRequestInterface {

    private String updateBeat;
    private MultipartFile mp3AudioFile;
    private MultipartFile wavAudioFile;
    private MultipartFile image;
    @Size(min = 3, max = 10, message = "The genre must consist of 3 to 10 characters.")
    private String genre;
    @Size(min = 3, max = 20, message = "The mood must consist of 3 to 20 characters.")
    private String mood;
    @Size(min = 2, max = 3, message = "The bpm must consist of 2 to 3 characters.")
    private String bpm;
    @Size(min = 3, max = 5, message = "The key must consist of 3 to 5 characters.")
    private String key;

    public void setGenre(String genre) {
        this.genre = genre.trim();
    }

    public void setMood(String mood) {
        this.mood = mood.trim();
    }

    public void setBpm(String bpm) {
        this.bpm = bpm.trim();
    }

    public void setKey(String key) {
        this.key = key.trim();
    }
}
