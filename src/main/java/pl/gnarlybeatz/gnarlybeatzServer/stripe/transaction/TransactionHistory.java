package pl.gnarlybeatz.gnarlybeatzServer.stripe.transaction;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import pl.gnarlybeatz.gnarlybeatzServer.audioFilesManagement.fileData.FileData;
import pl.gnarlybeatz.gnarlybeatzServer.user.User;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TransactionHistory")
public class TransactionHistory {
    @Id
    @GeneratedValue
    private Long id;
    private String sessionId;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beat_id")
    private FileData beat;

}
