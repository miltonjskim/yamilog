package com.yamilog.categoryservice.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class Category {
    String categoryId;
    String name;
    String description;
    String iconUrl;
    boolean active;
    List<EvaluationField> fields;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
