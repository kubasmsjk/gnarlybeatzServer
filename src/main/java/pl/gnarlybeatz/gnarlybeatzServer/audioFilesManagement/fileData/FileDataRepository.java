package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.fileData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
    Optional<FileData> findByNameAndAudioFileType(String fileName, String fileType);

    List<FileData> findBeatsListByName(String fileName);

    Optional<FileData> findByProductStripePriceId(String id);

    @Query("SELECT e FROM FileData e WHERE e.audioFileType = :audioFileType")
    Set<FileData> getAllProductsByType(@Param("audioFileType") String audioFileType);

    @Query("SELECT e FROM FileData e WHERE e.audioFileType = :audioFileType and e.isActive =true")
    Set<FileData> getAllMp3ArchiveByType(@Param("audioFileType") String audioFileType);

    @Query("SELECT e.productStripePriceId FROM FileData e WHERE e.name = :name and e.audioFileType = :audioFileType")
    String productStripePriceIdByType(@Param("name") String name, @Param("audioFileType") String audioFileType);

    @Query("SELECT DISTINCT e.bpm FROM FileData e WHERE e.isActive = true ORDER BY e.bpm ASC")
    Set<String> findDistinctBpm();

    @Query("SELECT DISTINCT e.key FROM FileData e WHERE e.isActive = true ORDER BY e.key ASC")
    Set<String> findDistinctKey();

    @Query("SELECT DISTINCT e.mood FROM FileData e WHERE e.isActive = true ORDER BY e.mood ASC")
    Set<String> findDistinctMood();

    @Query("SELECT DISTINCT e.genre FROM FileData e WHERE e.isActive = true ORDER BY e.genre ASC")
    Set<String> findDistinctGenre();

    @Query("SELECT DISTINCT e.bpm FROM FileData e JOIN FavoriteBeats fb ON e.id = fb.beat.id WHERE fb.user.id = :userId AND e.isActive = true ORDER BY e.bpm ASC")
    Set<String> findDistinctBpmByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT e.key FROM FileData e JOIN FavoriteBeats fb ON e.id = fb.beat.id WHERE fb.user.id = :userId AND e.isActive = true  ORDER BY e.key ASC")
    Set<String> findDistinctKeyByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT e.mood FROM FileData e JOIN FavoriteBeats fb ON e.id = fb.beat.id WHERE fb.user.id = :userId AND e.isActive = true  ORDER BY e.mood ASC")
    Set<String> findDistinctMoodByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT e.genre FROM FileData e JOIN FavoriteBeats fb ON e.id = fb.beat.id WHERE fb.user.id = :userId AND e.isActive = true  ORDER BY e.genre ASC")
    Set<String> findDistinctGenreByUserId(@Param("userId") Long userId);
}
