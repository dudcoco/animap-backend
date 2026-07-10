// 응답용 DTO를 따로 만들어서 필요한 값만 내려줌
package com.animap.backend.domain.user.dto;

import com.animap.backend.domain.user.User;

public record UserInfoResponse(
        Long userId,
        String email,
        String nickname
) {
    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname()
        );
    }
}