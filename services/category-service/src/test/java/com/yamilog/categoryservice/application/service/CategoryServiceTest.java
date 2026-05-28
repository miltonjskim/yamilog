package com.yamilog.categoryservice.application.service;

import com.yamilog.categoryservice.application.port.in.CreateCategoryCommand;
import com.yamilog.categoryservice.application.port.in.GetCategorySchemaQuery;
import com.yamilog.categoryservice.application.port.in.UpdateCategorySchemaCommand;
import com.yamilog.categoryservice.application.port.out.CategoryEventPublisher;
import com.yamilog.categoryservice.application.port.out.CategoryRepository;
import com.yamilog.categoryservice.domain.event.CategorySchemaUpdatedEvent;
import com.yamilog.categoryservice.domain.exception.CategoryErrorCode;
import com.yamilog.categoryservice.domain.model.Category;
import com.yamilog.categoryservice.domain.model.EvaluationField;
import com.yamilog.categoryservice.domain.model.FieldType;
import com.yamilog.common.domain.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock CategoryRepository categoryRepository;
    @Mock CategoryEventPublisher eventPublisher;

    @InjectMocks CategoryService categoryService;

    @Test
    @DisplayName("카테고리 생성 시 저장 후 반환된다")
    void create_savesAndReturnsCategory() {
        given(categoryRepository.existsByName("필터 커피")).willReturn(false);
        given(categoryRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        var fields = List.of(EvaluationField.builder()
            .fieldKey("acidity").displayName("산미").fieldType(FieldType.SCORE_1_10)
            .required(true).sortOrder(1).build());

        Category result = categoryService.create(
            new CreateCategoryCommand("필터 커피", "핸드드립 커피", null, fields)
        );

        assertThat(result.getName()).isEqualTo("필터 커피");
        assertThat(result.getFields()).hasSize(1);
        then(categoryRepository).should().save(any());
    }

    @Test
    @DisplayName("중복 카테고리 이름으로 생성 시 CATEGORY_NAME_DUPLICATE 예외가 발생한다")
    void create_duplicateName_throwsException() {
        given(categoryRepository.existsByName("필터 커피")).willReturn(true);

        assertThatThrownBy(() ->
            categoryService.create(new CreateCategoryCommand("필터 커피", null, null, List.of()))
        )
            .isInstanceOf(BusinessException.class)
            .extracting(e -> ((BusinessException) e).getErrorCode())
            .isEqualTo(CategoryErrorCode.CATEGORY_NAME_DUPLICATE);
    }

    @Test
    @DisplayName("SELECT 타입 필드에 options 없으면 INVALID_FIELD_OPTIONS 예외가 발생한다")
    void create_selectFieldWithoutOptions_throwsException() {
        given(categoryRepository.existsByName(any())).willReturn(false);

        var fields = List.of(EvaluationField.builder()
            .fieldKey("style").displayName("스타일").fieldType(FieldType.SELECT)
            .required(true).sortOrder(1).options(List.of()).build());

        assertThatThrownBy(() ->
            categoryService.create(new CreateCategoryCommand("위스키", null, null, fields))
        )
            .isInstanceOf(BusinessException.class)
            .extracting(e -> ((BusinessException) e).getErrorCode())
            .isEqualTo(CategoryErrorCode.INVALID_FIELD_OPTIONS);
    }

    @Test
    @DisplayName("스키마 업데이트 시 CategorySchemaUpdatedEvent가 발행된다")
    void updateSchema_publishesCategorySchemaUpdatedEvent() {
        Category existing = makeCategory("cat_001");
        given(categoryRepository.findById("cat_001")).willReturn(Optional.of(existing));
        given(categoryRepository.save(any())).willAnswer(inv -> inv.getArgument(0));

        var fields = List.of(EvaluationField.builder()
            .fieldKey("roast").displayName("로스팅").fieldType(FieldType.SCORE_1_10)
            .required(false).sortOrder(1).build());

        categoryService.updateSchema(new UpdateCategorySchemaCommand("cat_001", fields));

        then(eventPublisher).should().publish(any(CategorySchemaUpdatedEvent.class));
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 조회 시 CATEGORY_NOT_FOUND 예외가 발생한다")
    void getSchema_notFound_throwsException() {
        given(categoryRepository.findById("unknown")).willReturn(Optional.empty());

        assertThatThrownBy(() ->
            categoryService.getSchema(new GetCategorySchemaQuery("unknown"))
        )
            .isInstanceOf(BusinessException.class)
            .extracting(e -> ((BusinessException) e).getErrorCode())
            .isEqualTo(CategoryErrorCode.CATEGORY_NOT_FOUND);
    }

    private Category makeCategory(String id) {
        return Category.builder()
            .categoryId(id).name("필터 커피").description("핸드드립").active(true)
            .fields(List.of()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
            .build();
    }
}
