package com.yamilog.categoryservice.application.port.in;

import com.yamilog.categoryservice.domain.model.Category;

public interface UpdateCategorySchemaUseCase {
    Category updateSchema(UpdateCategorySchemaCommand command);
}
