package com.animap.backend.domain.preference.dto;

import com.animap.backend.domain.preference.UserPreferenceTag;

import java.math.BigDecimal;

public record PreferenceTagResponse(
        Long tagId,
        String name,
        String category,
        BigDecimal preferenceScore
) {
    public static PreferenceTagResponse from(UserPreferenceTag userPreferenceTag) {
        return new PreferenceTagResponse(
                userPreferenceTag.getTag().getId(),
                userPreferenceTag.getTag().getName(),
                userPreferenceTag.getTag().getCategory().name(),
                userPreferenceTag.getPreferenceScore()
        );
    }
}