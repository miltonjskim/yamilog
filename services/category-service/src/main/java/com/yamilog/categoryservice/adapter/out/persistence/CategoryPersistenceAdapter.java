package com.yamilog.categoryservice.adapter.out.persistence;

import com.yamilog.categoryservice.application.port.out.CategoryRepository;
import com.yamilog.categoryservice.domain.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class CategoryPersistenceAdapter implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;
    private final CategoryMapper mapper;

    @Override
    public Category save(Category category) {
        CategoryEntity entity = jpaRepository.findByPublicId(UUID.fromString(category.getCategoryId()))
            .map(existing -> {
                existing.setName(category.getName());
                existing.setDescription(category.getDescription());
                existing.setIconUrl(category.getIconUrl());
                existing.setActive(category.isActive());
                existing.getFields().clear();
                existing.getFields().addAll(buildFieldEntities(category, existing));
                return existing;
            })
            .orElseGet(() -> {
                CategoryEntity newEntity = mapper.toEntity(category);
                newEntity.getFields().addAll(buildFieldEntities(category, newEntity));
                return newEntity;
            });
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Category> findById(String categoryId) {
        return jpaRepository.findByPublicId(UUID.fromString(categoryId)).map(mapper::toDomain);
    }

    @Override
    public List<Category> findAllActive() {
        return jpaRepository.findAllByActiveTrue().stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    private List<EvaluationFieldEntity> buildFieldEntities(Category category, CategoryEntity parent) {
        return category.getFields().stream()
            .map(f -> {
                EvaluationFieldEntity fe = mapper.fieldToEntity(f);
                fe.setCategory(parent);
                return fe;
            })
            .toList();
    }
}
