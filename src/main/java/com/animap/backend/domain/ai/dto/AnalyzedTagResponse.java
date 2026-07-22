package com.animap.backend.domain.ai.dto;

public record AnalyzedTagResponse(
        String name,
        String category,
        int confidence
) {
}