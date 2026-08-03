package com.animap.backend.domain.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감상평 AI 분석 요청")
public record ReviewAnalysisRequest(

        @Schema(
                description = "AI가 분석할 사용자의 애니메이션 감상평",
                example = "캐릭터의 성장 과정과 액션 연출은 좋았지만 잔혹한 장면은 부담스러웠다.",
                maxLength = 1000
        )
        @NotBlank(message = "감상 이유는 필수입니다.")
        @Size(max = 1000, message = "감상 이유는 1000자 이하로 입력해주세요.")
        String reviewText
) {
}