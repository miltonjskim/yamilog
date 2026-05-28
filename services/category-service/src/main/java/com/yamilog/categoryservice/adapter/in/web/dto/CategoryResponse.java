package com.yamilog.categoryservice.adapter.in.web.dto;

import com.yamilog.categoryservice.domain.model.Category;

import java.util.List;

public record CategoryResponse(
    String categoryId,
    String name,
    String description,
    String iconUrl,
    boolean active,
    int fieldCount
) {
    public static CategoryResponse of(Category category) {
        return new CategoryResponse(
            category.getCategoryId(),
            category.getName(),
            category.getDescription(),
            category.getIconUrl(),
            category.isActive(),
            category.getFields() != null ? category.getFields().size() : 0
        );
    }

    public static List<CategoryResponse> ofList(List<Category> categories) {
        return categories.stream().map(CategoryResponse::of).toList();
    }
}
