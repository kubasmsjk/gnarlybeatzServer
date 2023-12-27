package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FileData")
public class FileData {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String genre;
    private String mood;
    private String bpm;
    private String key;
    private String audioFiletype;
    private String audioFilePath;
    private String imagePath;
}
