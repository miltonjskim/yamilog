package com.yamilog.userservice.application.port.in;

import com.yamilog.userservice.domain.model.User;
import com.yamilog.userservice.domain.model.UserLevel;

import java.util.List;

public interface GetUserProfileUseCase {
    User getProfile(GetUserProfileQuery query);
    List<UserLevel> getLevels(GetUserProfileQuery query);
}
