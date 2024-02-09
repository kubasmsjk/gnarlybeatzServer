package pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.fileData;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.favoriteBeats.FavoriteBeats;
import pl.gnarlybeatz.gnarlybeatzServer.stripe.transaction.TransactionHistory;

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
    private String audioFileType;
    private String audioFilePath;
    private String imagePath;
    private String productStripeId;
    private String productStripePriceId;
    private boolean isActive;

    @OneToMany(mappedBy = "beat", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FavoriteBeats> favoriteBeats;
    @OneToMany(mappedBy = "beat", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TransactionHistory> transactionHistoryList;
}
