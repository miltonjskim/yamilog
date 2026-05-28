package com.yamilog.categoryservice.application.port.in;

import com.yamilog.categoryservice.domain.model.EvaluationField;

import java.util.List;

public record CreateCategoryCommand(
    String name,
    String description,
    String iconUrl,
    List<EvaluationField> fields
) {}
