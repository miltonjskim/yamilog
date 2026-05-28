package com.yamilog.categoryservice.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByPublicId(UUID publicId);
    List<CategoryEntity> findAllByActiveTrue();
    boolean existsByName(String name);
}
