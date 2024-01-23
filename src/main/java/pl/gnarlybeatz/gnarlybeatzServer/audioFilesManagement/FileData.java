package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy = "beat", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FavoriteBeats> favoriteBeats;
}
