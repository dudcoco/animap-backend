package com.animap.backend.domain.user.dto;

// 회원가입 성공 시 응답해줄 데이터

public record SignUpResponse(
        Long userId,
        String message
) {
    public static SignUpResponse of(Long userId) {
        return new SignUpResponse(userId, "회원가입이 완료되었습니다.");
    }
}