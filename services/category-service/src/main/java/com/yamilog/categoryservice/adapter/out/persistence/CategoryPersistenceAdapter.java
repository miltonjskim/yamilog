package com.yamilog.categoryservice.adapter.out.persistence;

import com.yamilog.categoryservice.application.port.out.CategoryRepository;
import com.yamilog.categoryservice.domain.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class CategoryPersistenceAdapter implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;
    private final CategoryMapper mapper;

    @Override
    public Category save(Category category) {
        CategoryEntity entity = mapper.toEntity(category);
        List<EvaluationFieldEntity> fieldEntities = category.getFields().stream()
            .map(f -> {
                EvaluationFieldEntity fe = mapper.fieldToEntity(f);
                fe.setCategory(entity);
                return fe;
            })
            .toList();
        entity.getFields().clear();
        entity.getFields().addAll(fieldEntities);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Category> findById(String categoryId) {
        return jpaRepository.findById(categoryId).map(mapper::toDomain);
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
}
