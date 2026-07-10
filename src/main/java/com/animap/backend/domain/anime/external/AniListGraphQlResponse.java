package com.animap.backend.domain.anime.external;

import java.util.List;

public record AniListGraphQlResponse(
        Data data
) {
    public record Data(
            Page Page,
            Media Media
    ) {
    }

    public record Page(
            List<Media> media
    ) {
    }

    public record Media(
            Integer id,
            Title title,
            String description,
            CoverImage coverImage,
            Integer seasonYear,
            Double averageScore,
            String status
    ) {
    }

    public record Title(
            String romaji,
            String english,
            String nativeTitle
    ) {
    }

    public record CoverImage(
            String large
    ) {
    }
}