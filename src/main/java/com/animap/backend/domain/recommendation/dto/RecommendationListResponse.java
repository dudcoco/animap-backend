package com.animap.backend.domain.recommendation.dto;

import java.util.List;

public record RecommendationListResponse(
        String message,
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