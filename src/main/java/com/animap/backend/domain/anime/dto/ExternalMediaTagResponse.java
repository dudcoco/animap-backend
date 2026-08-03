package com.animap.backend.domain.anime.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AniList 외부 상세 태그 정보")
public record ExternalMediaTagResponse(

        @Schema(
                description = "AniList 태그 식별자",
                example = "63"
        )
        Integer id,

        @Schema(
                description = "AniList 태그 이름",
                example = "Coming of Age"
        )
        String name,

        @Schema(
                description = "AniList 태그 카테고리",
                example = "Theme"
        )
        String category,

        @Schema(
                description = "AniList 태그 설명",
                example = "작품 속 인물이 성장하고 성숙해지는 과정을 다루는 태그"
        )
        String description,

        @Schema(
                description = "성인용 태그 여부",
                example = "false"
        )
        Boolean isAdult,

        @Schema(
                description = "일반적인 스포일러 태그 여부",
                example = "false"
        )
        Boolean isGeneralSpoiler,

        @Schema(
                description = "특정 작품의 스포일러 태그 여부",
                example = "false"
        )
        Boolean isMediaSpoiler
) {
}