package com.yamilog.common.infra.security;

import com.yamilog.common.domain.exception.BusinessException;
import com.yamilog.common.domain.exception.CommonErrorCode;
import com.yamilog.common.domain.model.ManiaLevel;
import com.yamilog.common.infra.security.annotation.RequireLevel;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Aspect
@Component
public class RequireLevelAspect {

    @Before("@annotation(requireLevel)")
    public void checkLevel(RequireLevel requireLevel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
            || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
            throw new BusinessException(CommonErrorCode.UNAUTHORIZED);
        }

        ManiaLevel required = requireLevel.value();
        String category = requireLevel.category();

        ManiaLevel userLevel = StringUtils.hasText(category)
            ? principal.getLevelForCategory(category)
            : principal.getMaxLevel();

        if (!userLevel.isAtLeast(required)) {
            throw new BusinessException(
                CommonErrorCode.LEVEL_INSUFFICIENT,
                String.format("이 기능은 %s 이상의 레벨이 필요합니다.", required.name())
            );
        }
    }
}
