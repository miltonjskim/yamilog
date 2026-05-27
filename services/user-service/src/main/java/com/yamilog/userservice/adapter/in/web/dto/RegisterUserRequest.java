package com.yamilog.userservice.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 2, max = 20) String nickname,
    @NotBlank @Size(min = 8, max = 64) String password
) {}
