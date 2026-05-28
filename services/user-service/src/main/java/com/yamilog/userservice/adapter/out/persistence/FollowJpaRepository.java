package com.yamilog.userservice.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

interface FollowJpaRepository extends JpaRepository<FollowEntity, Long> {

    Optional<FollowEntity> findByFollowerIdAndFolloweeId(String followerId, String followeeId);

    boolean existsByFollowerIdAndFolloweeId(String followerId, String followeeId);

    long countByFolloweeId(String userId);

    long countByFollowerId(String userId);

    @Modifying
    @Query("DELETE FROM FollowEntity f WHERE f.followerId = :followerId AND f.followeeId = :followeeId")
    void deleteByFollowerIdAndFolloweeId(String followerId, String followeeId);
}
