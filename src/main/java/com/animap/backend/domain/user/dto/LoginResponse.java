package com.animap.backend.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 성공 응답")
public record LoginResponse(
        @Schema(
                description = "로그인한 사용자의 식별자",
                example = "1"
        )
        Long userId,

        @Schema(
                description = "로그인한 사용자의 닉네임",
                example = "테스터"
        )
        String nickname,

        @Schema(
                description = "API 인증에 사용하는 JWT 액세스 토큰",
                example = "eyJhbGciOiJIUzI1NiJ9..."
        )
        String accessToken,

        @Schema(
                description = "로그인 결과 메시지",
                example = "로그인에 성공했습니다."
        )
        String message
) {
    public static LoginResponse of(Long userId, String nickname, String accessToken) {
        return new LoginResponse(userId, nickname, accessToken, "로그인에 성공했습니다.");
    }
}