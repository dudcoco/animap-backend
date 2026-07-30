package com.animap.backend.domain.preference.dto;

import com.animap.backend.domain.preference.UserPreferenceTag;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 선호 태그 조회 응답")
public record PreferenceTagResponse(

        @Schema(
                description = "태그의 식별자",
                example = "3"
        )
        Long tagId,

        @Schema(
                description = "태그 이름",
                example = "닌자·사무라이"
        )
        String name,

        @Schema(
                description = "태그 카테고리",
                example = "THEME"
        )
        String category,

        @Schema(
                description = "사용자의 태그 선호 점수",
                example = "8.50"
        )
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