package com.yamilog.userservice.application.port.in;

import com.yamilog.userservice.domain.model.User;

public interface RegisterUserUseCase {
    User register(RegisterUserCommand command);
}
