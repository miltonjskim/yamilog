package com.yamilog.userservice.application.port.in;

import com.yamilog.userservice.domain.model.ProviderType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserCommand(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 2, max = 20) String nickname,
    String password,          // LOCAL 가입 시 필수
    ProviderType providerType,
    String providerId         // OAuth2 가입 시 필수
) {
    public static RegisterUserCommand local(String email, String nickname, String password) {
        return new RegisterUserCommand(email, nickname, password, ProviderType.LOCAL, null);
    }

    public static RegisterUserCommand oauth(String email, String nickname,
                                            ProviderType provider, String providerId) {
        return new RegisterUserCommand(email, nickname, null, provider, providerId);
    }
}
