package com.yamilog.categoryservice.application.service;

import com.yamilog.categoryservice.application.port.in.*;
import com.yamilog.categoryservice.application.port.out.CategoryEventPublisher;
import com.yamilog.categoryservice.application.port.out.CategoryRepository;
import com.yamilog.categoryservice.domain.event.CategorySchemaUpdatedEvent;
import com.yamilog.categoryservice.domain.exception.CategoryErrorCode;
import com.yamilog.categoryservice.domain.model.Category;
import com.yamilog.categoryservice.domain.model.EvaluationField;
import com.yamilog.categoryservice.domain.model.FieldType;
import com.yamilog.common.domain.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService implements
    GetCategorySchemaUseCase,
    ListCategoriesUseCase,
    CreateCategoryUseCase,
    UpdateCategorySchemaUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryEventPublisher eventPublisher;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "categorySchema", key = "#query.categoryId()")
    public Category getSchema(GetCategorySchemaQuery query) {
        return categoryRepository.findById(query.categoryId())
            .orElseThrow(() -> new BusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "categories")
    public List<Category> listActive() {
        return categoryRepository.findAllActive();
    }

    @Override
    @Transactional
    public Category create(CreateCategoryCommand command) {
        if (categoryRepository.existsByName(command.name())) {
            throw new BusinessException(CategoryErrorCode.CATEGORY_NAME_DUPLICATE);
        }
        validateFields(command.fields());

        Category category = Category.builder()
            .categoryId(UUID.randomUUID().toString())
            .name(command.name())
            .description(command.description())
            .iconUrl(command.iconUrl())
            .active(true)
            .fields(command.fields())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    @CacheEvict(value = "categorySchema", key = "#command.categoryId()")
    public Category updateSchema(UpdateCategorySchemaCommand command) {
        Category existing = categoryRepository.findById(command.categoryId())
            .orElseThrow(() -> new BusinessException(CategoryErrorCode.CATEGORY_NOT_FOUND));

        validateFields(command.fields());

        Category updated = Category.builder()
            .categoryId(existing.getCategoryId())
            .name(existing.getName())
            .description(existing.getDescription())
            .iconUrl(existing.getIconUrl())
            .active(existing.isActive())
            .fields(command.fields())
            .createdAt(existing.getCreatedAt())
            .updatedAt(LocalDateTime.now())
            .build();

        Category saved = categoryRepository.save(updated);
        eventPublisher.publish(CategorySchemaUpdatedEvent.of(saved.getCategoryId()));
        return saved;
    }

    private void validateFields(List<EvaluationField> fields) {
        long uniqueKeys = fields.stream().map(EvaluationField::getFieldKey).distinct().count();
        if (uniqueKeys != fields.size()) {
            throw new BusinessException(CategoryErrorCode.FIELD_KEY_DUPLICATE);
        }
        for (EvaluationField f : fields) {
            if (f.getFieldType() == FieldType.SELECT &&
                (f.getOptions() == null || f.getOptions().isEmpty())) {
                throw new BusinessException(CategoryErrorCode.INVALID_FIELD_OPTIONS);
            }
        }
    }
}
