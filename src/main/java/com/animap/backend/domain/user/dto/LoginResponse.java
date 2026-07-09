package com.animap.backend.domain.user.dto;

public record LoginResponse(
        Long userId,
        String nickname,
        String message
) {
    public static LoginResponse of(Long userId, String nickname) {
        return new LoginResponse(userId, nickname, "로그인에 성공했습니다.");
    }
}