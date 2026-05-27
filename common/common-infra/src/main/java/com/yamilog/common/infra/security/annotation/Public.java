package com.yamilog.common.infra.security.annotation;

import java.lang.annotation.*;

/**
 * 인증 없이 접근 가능한 공개 API에 붙인다.
 * Security 설정에서 이 어노테이션이 붙은 엔드포인트를 permitAll 처리한다.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Public {
}
