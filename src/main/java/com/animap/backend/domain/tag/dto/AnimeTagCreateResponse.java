package com.animap.backend.domain.tag.dto;

public record AnimeTagCreateResponse(
        Long animeId,
        Long tagId,
        String tagName,
        String category,
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