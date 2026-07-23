package com.animap.backend.domain.analysis.dto;

public record RecordAiAnalysisResponse(
        Long recordId,
        int positiveTagCount,
        int negativeTagCount,
        int reflectedTagCount,
        String summary,
        String message
) {
    public static RecordAiAnalysisResponse of(
            Long recordId,
            int positiveTagCount,
            int negativeTagCount,
            String summary
    ) {
        return new RecordAiAnalysisResponse(
                recordId,
                positiveTagCount,
                negativeTagCount,
                positiveTagCount + negativeTagCount,
                summary,
                "감상 기록의 AI 분석 결과가 사용자 선호 태그에 반영되었습니다."
        );
    }
}