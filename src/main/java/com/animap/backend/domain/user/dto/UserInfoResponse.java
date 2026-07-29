// 응답용 DTO를 따로 만들어서 필요한 값만 내려줌
package com.animap.backend.domain.user.dto;

import com.animap.backend.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인한 사용자 정보 응답")
public record UserInfoResponse(

        @Schema(
                description = "사용자의 식별자",
                example = "1"
        )
        Long userId,

        @Schema(
                description = "사용자의 이메일",
                example = "test@example.com"
        )
        String email,

        @Schema(
                description = "사용자의 닉네임",
                example = "테스터"
        )
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