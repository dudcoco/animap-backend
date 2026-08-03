package com.animap.backend.domain.anime.dto;

import com.animap.backend.domain.tag.AnimeTag;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "애니메이션에 연결된 표준 태그 정보")
public record AnimeTagResponse(

        @Schema(
                description = "표준 태그 식별자",
                example = "3"
        )
        Long tagId,

        @Schema(
                description = "표준 태그 이름",
                example = "성장"
        )
        String name,

        @Schema(
                description = "표준 태그 카테고리",
                example = "THEME"
        )
        String category,

        @Schema(
                description = "해당 애니메이션에서 태그가 가지는 가중치",
                example = "4",
                minimum = "1",
                maximum = "5"
        )
        Byte weight
) {
    public static AnimeTagResponse from(AnimeTag animeTag) {
        return new AnimeTagResponse(
                animeTag.getTag().getId(),
                animeTag.getTag().getName(),
                animeTag.getTag().getCategory().name(),
                animeTag.getWeight()
        );
    }
}