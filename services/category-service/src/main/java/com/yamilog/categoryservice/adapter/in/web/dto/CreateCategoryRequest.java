package com.yamilog.categoryservice.adapter.in.web.dto;

import com.yamilog.categoryservice.domain.model.FieldType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateCategoryRequest(
    @NotBlank @Size(max = 50) String name,
    @Size(max = 200) String description,
    String iconUrl,
    @NotEmpty @Valid List<EvaluationFieldDto> fields
) {
    public record EvaluationFieldDto(
        @NotBlank @Size(max = 50) String fieldKey,
        @NotBlank @Size(max = 50) String displayName,
        FieldType fieldType,
        List<String> options,
        boolean required,
        int sortOrder
    ) {}
}
