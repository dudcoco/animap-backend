package com.animap.backend.domain.tag.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "애니메이션 태그 등록 응답")
public record AnimeTagCreateResponse(

        @Schema(
                description = "태그가 연결된 애니메이션의 식별자",
                example = "1"
        )
        Long animeId,

        @Schema(
                description = "등록되거나 이미 연결되어 있던 태그의 식별자",
                example = "5"
        )
        Long tagId,

        @Schema(
                description = "태그 이름",
                example = "성장"
        )
        String tagName,

        @Schema(
                description = "태그 카테고리",
                example = "THEME"
        )
        String category,

        @Schema(
                description = "태그 등록 처리 결과 메시지",
                example = "애니메이션에 태그가 등록되었습니다."
        )
        String message
) {
    public static AnimeTagCreateResponse of(
            Long animeId,
            Long tagId,
            String tagName,
            String category
    ) {
        return new AnimeTagCreateResponse(
                animeId,
                tagId,
                tagName,
                category,
                "애니메이션에 태그가 등록되었습니다."
        );
    }

    public static AnimeTagCreateResponse alreadyExists(
            Long animeId,
            Long tagId,
            String tagName,
            String category
    ) {
        return new AnimeTagCreateResponse(
                animeId,
                tagId,
                tagName,
                category,
                "이미 등록된 태그입니다."
        );
    }
}