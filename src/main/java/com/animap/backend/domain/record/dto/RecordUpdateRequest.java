package com.animap.backend.domain.record.dto;

import com.animap.backend.domain.record.PreferenceType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

// 수정 가능: rating, preferenceType, reviewText
// 수정하지 않음: user, anime
@Schema(description = "감상 기록 수정 요청")
public record RecordUpdateRequest(

        @Schema(
                description = "수정할 평점. 전달하지 않으면 기존 값을 유지",
                example = "9",
                minimum = "1",
                maximum = "10",
                nullable = true
        )
        @Min(value = 1, message = "평점은 1점 이상이어야 합니다.")
        @Max(value = 10, message = "평점은 10점 이하이어야 합니다.")
        Integer rating,

        @Schema(
                description = "수정할 선호 상태. 전달하지 않으면 기존 값을 유지",
                example = "LIKE",
                nullable = true
        )
        PreferenceType preferenceType,

        @Schema(
                description = "수정할 감상평. 전달하지 않으면 기존 값을 유지",
                example = "후반부 전개와 캐릭터 관계가 특히 좋았다.",
                maxLength = 1000,
                nullable = true
        )
        @Size(max = 1000, message = "감상 이유는 1000자 이하로 입력해주세요.")
        String reviewText
) {
}