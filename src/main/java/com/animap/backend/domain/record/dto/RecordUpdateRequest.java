package com.animap.backend.domain.record.dto;

import com.animap.backend.domain.record.PreferenceType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

// 수정 가능: rating, preferenceType, reviewText
// 수정하지 않음: user, anime
public record RecordUpdateRequest(

        @Min(value = 1, message = "평점은 1점 이상이어야 합니다.")
        @Max(value = 10, message = "평점은 10점 이하이어야 합니다.")
        Integer rating,

        PreferenceType preferenceType,

        @Size(max = 1000, message = "감상 이유는 1000자 이하로 입력해주세요.")
        String reviewText
) {
}