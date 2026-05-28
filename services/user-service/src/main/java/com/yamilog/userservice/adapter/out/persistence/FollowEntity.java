package com.yamilog.userservice.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "follows",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_follows_follower_followee",
        columnNames = {"follower_id", "followee_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq_id", updatable = false)
    private Long seqId;

    @Column(name = "follower_id", nullable = false, length = 36)
    private String followerId;

    @Column(name = "followee_id", nullable = false, length = 36)
    private String followeeId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
