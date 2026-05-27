package com.yamilog.common.infra.security.annotation;

import com.yamilog.common.domain.model.ManiaLevel;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireLevel {
    ManiaLevel value();

    /**
     * 특정 카테고리 기준 레벨 검사. 빈 문자열이면 전체 카테고리 중 최고 레벨 기준.
     */
    String category() default "";
}
