package com.yamilog.userservice.adapter.in.web;

import com.yamilog.common.infra.security.UserPrincipal;
import com.yamilog.common.infra.security.annotation.Public;
import com.yamilog.common.infra.web.ApiResponse;
import com.yamilog.userservice.adapter.in.web.dto.RegisterUserRequest;
import com.yamilog.userservice.adapter.in.web.dto.UserProfileResponse;
import com.yamilog.userservice.application.port.in.GetUserProfileQuery;
import com.yamilog.userservice.application.port.in.GetUserProfileUseCase;
import com.yamilog.userservice.application.port.in.RegisterUserCommand;
import com.yamilog.userservice.application.port.in.RegisterUserUseCase;
import com.yamilog.userservice.domain.model.User;
import com.yamilog.userservice.domain.model.UserLevel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final GetUserProfileUseCase getUserProfileUseCase;

    @Public
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserProfileResponse>> register(
        @Valid @RequestBody RegisterUserRequest request
    ) {
        User user = registerUserUseCase.register(
            RegisterUserCommand.local(request.email(), request.nickname(), request.password())
        );
        return ResponseEntity.status(201).body(
            ApiResponse.created(UserProfileResponse.of(user, List.of()))
        );
    }

    @Public
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
        @PathVariable String userId
    ) {
        GetUserProfileQuery query = new GetUserProfileQuery(userId);
        User user = getUserProfileUseCase.getProfile(query);
        List<UserLevel> levels = getUserProfileUseCase.getLevels(query);
        return ResponseEntity.ok(ApiResponse.success(UserProfileResponse.of(user, levels)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getMyProfile(
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        GetUserProfileQuery query = new GetUserProfileQuery(principal.getUserId());
        User user = getUserProfileUseCase.getProfile(query);
        List<UserLevel> levels = getUserProfileUseCase.getLevels(query);
        return ResponseEntity.ok(ApiResponse.success(UserProfileResponse.of(user, levels)));
    }
}
