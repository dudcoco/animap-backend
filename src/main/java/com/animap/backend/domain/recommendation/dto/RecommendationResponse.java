package com.animap.backend.domain.recommendation.dto;

import java.math.BigDecimal;
import java.util.List;

public record RecommendationResponse(
        Long animeId,
        String titleKo,
        String titleOriginal,
        String posterUrl,
        Short releaseYear,
        BigDecimal recommendationScore,
        List<String> matchedTags,
        String reason
) {
}