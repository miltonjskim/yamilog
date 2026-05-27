package com.yamilog.userservice.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
@IdClass(FollowEntity.FollowId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowEntity {

    @Id
    @Column(name = "follower_id", nullable = false, length = 32)
    private String followerId;

    @Id
    @Column(name = "followee_id", nullable = false, length = 32)
    private String followeeId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @lombok.Value
    public static class FollowId implements Serializable {
        String followerId;
        String followeeId;
    }
}
