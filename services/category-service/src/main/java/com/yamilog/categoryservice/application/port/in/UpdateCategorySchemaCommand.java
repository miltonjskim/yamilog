package com.yamilog.categoryservice.application.port.in;

import com.yamilog.categoryservice.domain.model.EvaluationField;

import java.util.List;

public record UpdateCategorySchemaCommand(
    String categoryId,
    List<EvaluationField> fields
) {}
