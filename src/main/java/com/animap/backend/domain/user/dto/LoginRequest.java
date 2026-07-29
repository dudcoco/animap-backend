package com.animap.backend.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 요청")
public record LoginRequest(

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Schema(
                description = "사용자 이메일",
                example = "test@example.com"
        )
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Schema(
                description = "사용자 비밀번호",
                example = "password123"
        )
        String password
) {
}