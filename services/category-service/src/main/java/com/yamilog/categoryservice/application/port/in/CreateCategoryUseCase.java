package com.yamilog.categoryservice.application.port.in;

import com.yamilog.categoryservice.domain.model.Category;

public interface CreateCategoryUseCase {
    Category create(CreateCategoryCommand command);
}
