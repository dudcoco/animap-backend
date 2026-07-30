package com.animap.backend.domain.tag.dto;

import com.animap.backend.domain.tag.TagType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "애니메이션 태그 등록 요청")
public record AnimeTagCreateRequest(

        @Schema(
                description = "애니메이션에 등록할 태그 이름",
                example = "성장",
                maxLength = 100
        )
        @NotBlank(message = "태그 이름은 필수입니다.")
        @Size(max = 100, message = "태그 이름은 100자 이하로 입력해주세요.")
        String name,

        @Schema(
                description = "태그 카테고리",
                example = "THEME"
        )
        @NotNull(message = "태그 카테고리는 필수입니다.")
        TagType category,

        @Schema(
                description = "애니메이션에서 해당 태그가 가지는 가중치. 1부터 5까지 입력 가능",
                example = "4",
                minimum = "1",
                maximum = "5",
                nullable = true
        )
        @Min(value = 1, message = "가중치는 1 이상이어야 합니다.")
        @Max(value = 5, message = "가중치는 5 이하이어야 합니다.")
        Integer weight
) {
}