package com.animap.backend.domain.recommendation.dto;

import java.math.BigDecimal;
import java.util.List;

public record RecommendationResponse(
        Long animeId,
        String titleKo,
        String titleOriginal,
        String posterUrl,
        Short releaseYear,
        BigDecimal rawScore, // 추천 로직이 실제로 계산한 내부 점수
        BigDecimal matchPercent, // 현재 추천후보 중 최고점수를 100으로 보고 환산한 상대 적합도
        List<String> matchedTags,
        List<String> cautionTags,
        String reason,
        String cautionMessage
) {
}