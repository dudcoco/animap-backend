package com.animap.backend.domain.recommendation.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "애니메이션 추천 목록 조회 응답")
public record RecommendationListResponse(
        @Schema(
                description = "추천 목록 조회 결과 메시지",
                example = "추천 결과를 조회했습니다."
        )
        String message,

        @Schema(
                description = "사용자 취향을 기반으로 계산된 애니메이션 추천 목록"
        )
        List<RecommendationResponse> recommendations
) {
    public static RecommendationListResponse of(List<RecommendationResponse> recommendations) {
        if (recommendations.isEmpty()) {
            return new RecommendationListResponse(
                    "추천 가능한 애니가 아직 없습니다. 더 많은 감상 기록을 등록하거나 애니 태그를 추가해주세요.",
                    recommendations
            );
        }

        return new RecommendationListResponse(
                "추천 결과를 조회했습니다.",
                recommendations
        );
    }
}