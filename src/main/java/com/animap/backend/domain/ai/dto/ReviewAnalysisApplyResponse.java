package com.animap.backend.domain.ai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감상평 AI 분석 결과의 사용자 선호도 반영 응답")
public record ReviewAnalysisApplyResponse(

        @Schema(
                description = "긍정적으로 분석되어 반영된 태그 개수",
                example = "3"
        )
        int positiveTagCount,

        @Schema(
                description = "부정적으로 분석되어 반영된 태그 개수",
                example = "1"
        )
        int negativeTagCount,

        @Schema(
                description = "사용자 선호 태그에 반영된 전체 태그 개수",
                example = "4"
        )
        int reflectedTagCount,

        @Schema(
                description = "AI 분석 결과 반영 메시지",
                example = "AI 분석 결과가 사용자 선호 태그에 반영되었습니다."
        )
        String message
) {
    public static ReviewAnalysisApplyResponse of(
            int positiveTagCount,
            int negativeTagCount
    ) {
        return new ReviewAnalysisApplyResponse(
                positiveTagCount,
                negativeTagCount,
                positiveTagCount + negativeTagCount,
                "AI 분석 결과가 사용자 선호 태그에 반영되었습니다."
        );
    }
}