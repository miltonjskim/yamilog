package com.yamilog.categoryservice.adapter.out.persistence;

import com.yamilog.categoryservice.domain.model.Category;
import com.yamilog.categoryservice.domain.model.EvaluationField;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = java.util.UUID.class)
interface CategoryMapper {

    @Mapping(target = "categoryId", expression = "java(entity.getPublicId().toString())")
    Category toDomain(CategoryEntity entity);

    @Mapping(target = "publicId", expression = "java(UUID.fromString(category.getCategoryId()))")
    @Mapping(target = "seqId", ignore = true)
    @Mapping(target = "fields", ignore = true)
    CategoryEntity toEntity(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    EvaluationFieldEntity fieldToEntity(EvaluationField field);

    EvaluationField fieldToDomain(EvaluationFieldEntity entity);
}
