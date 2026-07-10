package com.animap.backend.domain.record.dto;

import com.animap.backend.domain.record.PreferenceType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RecordCreateRequest(

        @NotNull(message = "애니메이션 ID는 필수입니다.")
        Long animeId,

        @Min(value = 1, message = "평점은 1점 이상이어야 합니다.")
        @Max(value = 10, message = "평점은 10점 이하이어야 합니다.")
        Integer rating,

        @NotNull(message = "선호 타입은 필수입니다.")
        PreferenceType preferenceType,

        @Size(max = 1000, message = "감상 이유는 1000자 이하로 입력해주세요.")
        String reviewText
) {
}