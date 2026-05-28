package com.yamilog.categoryservice.application.port.in;

import com.yamilog.categoryservice.domain.model.Category;

public interface GetCategorySchemaUseCase {
    Category getSchema(GetCategorySchemaQuery query);
}
