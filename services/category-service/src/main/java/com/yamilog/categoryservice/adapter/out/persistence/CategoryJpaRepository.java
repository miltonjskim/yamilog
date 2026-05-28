package com.yamilog.categoryservice.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface CategoryJpaRepository extends JpaRepository<CategoryEntity, String> {
    List<CategoryEntity> findAllByActiveTrue();
    boolean existsByName(String name);
}
