package com.yamilog.common.infra.security;

import com.yamilog.common.domain.model.ManiaLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
@Builder
public class UserPrincipal {

    private final String userId;
    private final String nickname;

    // key: categoryId, value: ManiaLevel.value
    @Builder.Default
    private final Map<String, Integer> levels = Collections.emptyMap();

    public ManiaLevel getLevelForCategory(String categoryId) {
        return ManiaLevel.fromValue(levels.getOrDefault(categoryId, 0));
    }

    public ManiaLevel getMaxLevel() {
        int max = levels.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        return ManiaLevel.fromValue(max);
    }
}
