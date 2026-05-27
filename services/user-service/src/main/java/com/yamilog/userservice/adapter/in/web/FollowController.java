package com.yamilog.userservice.adapter.in.web;

import com.yamilog.common.infra.security.UserPrincipal;
import com.yamilog.common.infra.web.ApiResponse;
import com.yamilog.userservice.application.port.in.FollowUserCommand;
import com.yamilog.userservice.application.port.in.FollowUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/{followeeId}")
@RequiredArgsConstructor
public class FollowController {

    private final FollowUserUseCase followUserUseCase;

    @PostMapping("/follow")
    public ResponseEntity<ApiResponse<Void>> follow(
        @PathVariable String followeeId,
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        followUserUseCase.follow(new FollowUserCommand(principal.getUserId(), followeeId));
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/follow")
    public ResponseEntity<ApiResponse<Void>> unfollow(
        @PathVariable String followeeId,
        @AuthenticationPrincipal UserPrincipal principal
    ) {
        followUserUseCase.unfollow(new FollowUserCommand(principal.getUserId(), followeeId));
        return ResponseEntity.ok(ApiResponse.success());
    }
}
