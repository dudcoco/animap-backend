package com.animap.backend.domain.preference.dto;

public record PreferenceAnalyzeResponse(
        int analyzedRecordCount,
        int updatedPreferenceTagCount,
        String message
) {
    public static PreferenceAnalyzeResponse of(
            int analyzedRecordCount,
            int updatedPreferenceTagCount
    ) {
        return new PreferenceAnalyzeResponse(
                analyzedRecordCount,
                updatedPreferenceTagCount,
                "사용자 선호 태그 분석이 완료되었습니다."
        );
    }
}