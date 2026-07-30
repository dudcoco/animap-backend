package com.animap.backend.domain.preference.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 취향 분석 응답")
public record PreferenceAnalyzeResponse(

        @Schema(
                description = "취향 분석에 반영된 감상 기록 개수",
                example = "5"
        )
        int analyzedRecordCount,

        @Schema(
                description = "재계산되어 저장된 사용자 선호 태그 개수",
                example = "12"
        )
        int updatedPreferenceTagCount,

        @Schema(
                description = "취향 분석 결과 메시지",
                example = "사용자 선호 태그 분석이 완료되었습니다."
        )
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