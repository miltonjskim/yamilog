package com.yamilog.categoryservice.application.port.in;

import com.yamilog.categoryservice.domain.model.Category;

import java.util.List;

public interface ListCategoriesUseCase {
    List<Category> listActive();
}
