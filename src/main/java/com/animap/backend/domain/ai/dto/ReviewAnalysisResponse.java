package com.animap.backend.domain.ai.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감상평 AI 분석 응답")
public record ReviewAnalysisResponse(

        @Schema(
                description = "감상평에서 긍정적으로 분석된 태그 목록"
        )
        List<AnalyzedTagResponse> positiveTags,

        @Schema(
                description = "감상평에서 부정적으로 분석된 태그 목록"
        )
        List<AnalyzedTagResponse> negativeTags,

        @Schema(
                description = "AI가 생성한 감상평 분석 요약",
                example = "캐릭터 성장과 액션 요소를 선호하지만 잔혹한 연출에는 부정적인 반응을 보였습니다."
        )
        String summary
) {
}