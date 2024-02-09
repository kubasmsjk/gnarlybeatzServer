package pl.gnarlybeatz.gnarlybeatzServer.stripe.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.fileData.FileData;
import pl.gnarlybeatz.gnarlybeatzServer.user.User;

import java.util.List;
import java.util.Optional;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    Optional<TransactionHistory> findByUserAndBeat(User user, FileData fileData);

    @Query("SELECT e.beat FROM TransactionHistory e WHERE e.user.id = :userId")
    List<FileData> findBeatByUserId(@Param("userId") Long userId);
}
