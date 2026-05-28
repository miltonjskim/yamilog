package com.yamilog.categoryservice.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class EvaluationField {
    String fieldKey;
    String displayName;
    FieldType fieldType;
    List<String> options;
    boolean required;
    int sortOrder;
}
