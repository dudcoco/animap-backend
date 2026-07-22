package com.animap.backend.domain.ai.dto;

import java.util.List;

public record ReviewAnalysisResponse(
        List<AnalyzedTagResponse> positiveTags,
        List<AnalyzedTagResponse> negativeTags,
        String summary
) {
}