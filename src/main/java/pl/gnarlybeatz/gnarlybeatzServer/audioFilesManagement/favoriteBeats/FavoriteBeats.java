package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.favoriteBeats;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.fileData.FileData;
import pl.gnarlybeatz.gnarlybeatzServer.user.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FavoriteBeats")
public class FavoriteBeats {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beat_id")
    private FileData beat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
