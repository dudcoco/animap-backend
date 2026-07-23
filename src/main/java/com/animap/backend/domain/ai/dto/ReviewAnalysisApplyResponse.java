package com.animap.backend.domain.ai.dto;

public record ReviewAnalysisApplyResponse(
        int positiveTagCount,
        int negativeTagCount,
        int reflectedTagCount,
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