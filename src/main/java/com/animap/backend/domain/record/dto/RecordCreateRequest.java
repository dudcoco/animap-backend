package com.animap.backend.domain.record.dto;

import com.animap.backend.domain.record.PreferenceType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감상 기록 등록 요청")
public record RecordCreateRequest(

        @Schema(
                description = "감상 기록을 등록할 애니메이션의 ID",
                example = "1"
        )
        @NotNull(message = "애니메이션 ID는 필수입니다.")
        Long animeId,

        @Schema(
                description = "사용자가 매긴 평점. 1점부터 10점까지 입력 가능",
                example = "8",
                minimum = "1",
                maximum = "10",
                nullable = true
        )
        @Min(value = 1, message = "평점은 1점 이상이어야 합니다.")
        @Max(value = 10, message = "평점은 10점 이하이어야 합니다.")
        Integer rating,

        @Schema(
                description = "애니메이션에 대한 사용자의 선호 타입",
                example = "LIKE"
        )
        @NotNull(message = "선호 타입은 필수입니다.")
        PreferenceType preferenceType,

        @Schema(
                description = "애니메이션에 대한 사용자의 감상평",
                example = "캐릭터의 성장 과정과 연출이 인상적이었다.",
                maxLength = 1000,
                nullable = true
        )
        @Size(max = 1000, message = "감상 이유는 1000자 이하로 입력해주세요.")
        String reviewText
) {
}