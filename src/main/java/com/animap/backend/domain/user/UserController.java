package com.animap.backend.domain.user;

import com.animap.backend.domain.user.dto.SignUpRequest;
import com.animap.backend.domain.user.dto.SignUpResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.animap.backend.domain.user.dto.LoginRequest;
import com.animap.backend.domain.user.dto.LoginResponse;
import com.animap.backend.domain.user.dto.UserInfoResponse;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        Long userId = userService.signUp(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SignUpResponse.of(userId));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        UserInfoResponse response = userService.getMyInfo(userId);

        return ResponseEntity.ok(response);
    }
}