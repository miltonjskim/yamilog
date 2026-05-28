package com.yamilog.categoryservice.adapter.in.web.dto;

import com.yamilog.categoryservice.domain.model.Category;
import com.yamilog.categoryservice.domain.model.EvaluationField;
import com.yamilog.categoryservice.domain.model.FieldType;

import java.util.List;

public record CategorySchemaResponse(
    String categoryId,
    String name,
    List<FieldDto> fields
) {
    public record FieldDto(
        String fieldKey,
        String displayName,
        FieldType fieldType,
        List<String> options,
        boolean required,
        int sortOrder
    ) {
        public static FieldDto of(EvaluationField field) {
            return new FieldDto(
                field.getFieldKey(),
                field.getDisplayName(),
                field.getFieldType(),
                field.getOptions(),
                field.isRequired(),
                field.getSortOrder()
            );
        }
    }

    public static CategorySchemaResponse of(Category category) {
        List<FieldDto> fields = category.getFields() == null ? List.of() :
            category.getFields().stream().map(FieldDto::of).toList();
        return new CategorySchemaResponse(category.getCategoryId(), category.getName(), fields);
    }
}
