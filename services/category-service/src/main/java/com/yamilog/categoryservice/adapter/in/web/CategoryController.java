package com.yamilog.categoryservice.adapter.in.web;

import com.yamilog.categoryservice.adapter.in.web.dto.CategoryResponse;
import com.yamilog.categoryservice.adapter.in.web.dto.CategorySchemaResponse;
import com.yamilog.categoryservice.adapter.in.web.dto.CreateCategoryRequest;
import com.yamilog.categoryservice.application.port.in.*;
import com.yamilog.categoryservice.domain.model.EvaluationField;
import com.yamilog.common.infra.security.annotation.Public;
import com.yamilog.common.infra.web.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ListCategoriesUseCase listCategoriesUseCase;
    private final GetCategorySchemaUseCase getCategorySchemaUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategorySchemaUseCase updateCategorySchemaUseCase;

    @Public
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> listCategories() {
        return ResponseEntity.ok(
            ApiResponse.success(CategoryResponse.ofList(listCategoriesUseCase.listActive()))
        );
    }

    @Public
    @GetMapping("/{categoryId}/schema")
    public ResponseEntity<ApiResponse<CategorySchemaResponse>> getSchema(
        @PathVariable String categoryId
    ) {
        return ResponseEntity.ok(
            ApiResponse.success(CategorySchemaResponse.of(
                getCategorySchemaUseCase.getSchema(new GetCategorySchemaQuery(categoryId))
            ))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
        @Valid @RequestBody CreateCategoryRequest request
    ) {
        List<EvaluationField> fields = request.fields().stream()
            .map(f -> EvaluationField.builder()
                .fieldKey(f.fieldKey())
                .displayName(f.displayName())
                .fieldType(f.fieldType())
                .options(f.options())
                .required(f.required())
                .sortOrder(f.sortOrder())
                .build())
            .toList();

        var category = createCategoryUseCase.create(
            new CreateCategoryCommand(request.name(), request.description(), request.iconUrl(), fields)
        );
        return ResponseEntity.status(201).body(ApiResponse.created(CategoryResponse.of(category)));
    }

    @PutMapping("/{categoryId}/schema")
    public ResponseEntity<ApiResponse<CategorySchemaResponse>> updateSchema(
        @PathVariable String categoryId,
        @Valid @RequestBody List<CreateCategoryRequest.EvaluationFieldDto> fieldDtos
    ) {
        List<EvaluationField> fields = fieldDtos.stream()
            .map(f -> EvaluationField.builder()
                .fieldKey(f.fieldKey())
                .displayName(f.displayName())
                .fieldType(f.fieldType())
                .options(f.options())
                .required(f.required())
                .sortOrder(f.sortOrder())
                .build())
            .toList();

        var category = updateCategorySchemaUseCase.updateSchema(
            new UpdateCategorySchemaCommand(categoryId, fields)
        );
        return ResponseEntity.ok(ApiResponse.success(CategorySchemaResponse.of(category)));
    }
}
