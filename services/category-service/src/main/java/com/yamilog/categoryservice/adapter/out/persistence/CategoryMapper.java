package com.yamilog.categoryservice.adapter.out.persistence;

import com.yamilog.categoryservice.domain.model.Category;
import com.yamilog.categoryservice.domain.model.EvaluationField;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface CategoryMapper {

    @Mapping(target = "categoryId", source = "id")
    @Mapping(target = "active", source = "active")
    Category toDomain(CategoryEntity entity);

    @Mapping(target = "id", source = "categoryId")
    @Mapping(target = "fields", ignore = true)
    CategoryEntity toEntity(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    EvaluationFieldEntity fieldToEntity(EvaluationField field);

    EvaluationField fieldToDomain(EvaluationFieldEntity entity);
}
