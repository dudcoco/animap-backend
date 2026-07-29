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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@Tag(
        name = "인증 및 사용자",
        description = "회원가입, 로그인, 사용자 정보 조회 API"
)
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "회원가입",
            description = "이메일, 비밀번호, 닉네임을 입력받아 새로운 사용자를 등록한다."
    )
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        Long userId = userService.signUp(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SignUpResponse.of(userId));
    }

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호를 검증하고, JWT 액세스 토큰을 반환한다."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "사용자 정보 조회",
            description = "로그인한 사용자의 이메일, 닉네임을 반환한다."
    )
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();

        UserInfoResponse response = userService.getMyInfo(userId);

        return ResponseEntity.ok(response);
    }
}