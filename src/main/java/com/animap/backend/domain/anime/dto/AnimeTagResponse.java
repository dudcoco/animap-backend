package com.animap.backend.domain.anime.dto;

import com.animap.backend.domain.tag.AnimeTag;

public record AnimeTagResponse(
        Long tagId,
        String name,
        String category,
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