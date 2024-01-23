package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.gnarlybeatz.gnarlybeatzServer.user.User;

import java.util.Optional;

public interface FavoriteBeatsRepository  extends JpaRepository<FavoriteBeats,Long> {
    Optional<FavoriteBeats> findByBeatAndUser(FileData beat, User user);
}
