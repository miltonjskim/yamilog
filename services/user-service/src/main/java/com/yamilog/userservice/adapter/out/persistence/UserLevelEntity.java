package com.yamilog.userservice.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_levels",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_user_levels_user_category",
        columnNames = {"user_id", "category_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLevelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_id", updatable = false)
    private Long seqId;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "category_id", nullable = false, length = 36)
    private String categoryId;

    @Column(name = "mania_level", nullable = false)
    private int maniaLevel;

    @Column(name = "quality_score", nullable = false)
    private int qualityScore;

    @Column(name = "review_count", nullable = false)
    private int reviewCount;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
