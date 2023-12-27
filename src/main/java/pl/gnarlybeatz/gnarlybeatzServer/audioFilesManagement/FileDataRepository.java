package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface FileDataRepository extends JpaRepository<FileData,Long> {
    Optional<FileData> findByName(String fileName);
    @Query("SELECT DISTINCT e.bpm FROM FileData e ORDER BY e.bpm ASC")
    Set<String> findDistinctBpm();
    @Query("SELECT DISTINCT e.key FROM FileData e ORDER BY e.key ASC")
    Set<String> findDistinctKey();
    @Query("SELECT DISTINCT e.mood FROM FileData e ORDER BY e.mood ASC")
    Set<String> findDistinctMood();
    @Query("SELECT DISTINCT e.genre FROM FileData e ORDER BY e.genre ASC")
    Set<String> findDistinctGenre();

//    @Query("SELECT DISTINCT new pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.FilterValues(e.bpm, e.key, e.mood, e.genre) FROM FileData e")
//    List<FilterValues> getFilterValues();


}
