package com.yamilog.categoryservice.application.port.out;

import com.yamilog.categoryservice.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(String categoryId);
    List<Category> findAllActive();
    boolean existsByName(String name);
}
