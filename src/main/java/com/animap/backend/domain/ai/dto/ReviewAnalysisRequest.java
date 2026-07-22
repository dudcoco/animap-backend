package com.animap.backend.domain.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewAnalysisRequest(

        @NotBlank(message = "감상 이유는 필수입니다.")
        @Size(max = 1000, message = "감상 이유는 1000자 이하로 입력해주세요.")
        String reviewText
) {
}