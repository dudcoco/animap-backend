package com.animap.backend.domain.tag.dto;

import com.animap.backend.domain.tag.TagType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AnimeTagCreateRequest(

        @NotBlank(message = "태그 이름은 필수입니다.")
        @Size(max = 100, message = "태그 이름은 100자 이하로 입력해주세요.")
        String name,

        @NotNull(message = "태그 카테고리는 필수입니다.")
        TagType category,

        @Min(value = 1, message = "가중치는 1 이상이어야 합니다.")
        @Max(value = 5, message = "가중치는 5 이하이어야 합니다.")
        Integer weight
) {
}