package com.animap.backend.domain.analysis.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감상 기록 AI 분석 응답")
public record RecordAiAnalysisResponse(

        @Schema(
                description = "AI 분석을 실행한 감상 기록의 식별자",
                example = "1"
        )
        Long recordId,

        @Schema(
                description = "긍정적으로 분석된 태그 개수",
                example = "1"
        )
        int positiveTagCount,

        @Schema(
                description = "부정적으로 분석된 태그 개수",
                example = "1"
        )
        int negativeTagCount,

        @Schema(
                description = "감상 기록 분석 결과로 저장된 전체 태그 개수",
                example = "4"
        )
        int reflectedTagCount,

        @Schema(
                description = "AI가 생성한 감상 기록 분석 요약",
                example = "캐릭터 성장과 액션 요소를 선호하고, 잔혹한 연출에는 부정적인 반응을 보였습니다."
        )
        String summary,

        @Schema(
                description = "AI 분석 결과 처리 메시지",
                example = "감상 기록의 AI 분석 결과가 사용자 선호 태그에 반영되었습니다."
        )
        String message
) {
    public static RecordAiAnalysisResponse of(
            Long recordId,
            int positiveTagCount,
            int negativeTagCount,
            int reflectedTagCount,
            String summary
    ) {
        return new RecordAiAnalysisResponse(
                recordId,
                positiveTagCount,
                negativeTagCount,
                reflectedTagCount,
                summary,
                "감상 기록의 AI 분석 결과가 저장되었습니다."
        );
    }
}
