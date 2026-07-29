package com.animap.backend.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

// 회원가입 성공 시 응답해줄 데이터
@Schema(description = "회원가입 성공 응답")
public record SignUpResponse(

        @Schema(
                description = "생성된 사용자의 식별자",
                example = "1"
        )
        Long userId,

        @Schema(
                description = "회원가입 결과 메시지",
                example = "회원가입이 완료되었습니다."
        )
        String message
) {
    public static SignUpResponse of(Long userId) {
        return new SignUpResponse(userId, "회원가입이 완료되었습니다.");
    }
}