package com.yamilog.categoryservice.adapter.out.persistence;

import com.yamilog.categoryservice.domain.model.FieldType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "evaluation_fields",
    uniqueConstraints = @UniqueConstraint(name = "uk_eval_fields_category_key",
        columnNames = {"category_id", "field_key"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationFieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @Column(name = "field_key", nullable = false, length = 50)
    private String fieldKey;

    @Column(name = "display_name", nullable = false, length = 50)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type", nullable = false, length = 20)
    private FieldType fieldType;

    @ElementCollection
    @CollectionTable(name = "evaluation_field_options",
        joinColumns = @JoinColumn(name = "field_id"))
    @Column(name = "option_value")
    private List<String> options;

    @Column(name = "required", nullable = false)
    private boolean required;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}
