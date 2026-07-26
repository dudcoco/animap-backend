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
            String status,
            List<String> genres,
            List<MediaTag> tags
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

    public record MediaTag(
            String name,
            Integer rank,
            Boolean isGeneralSpoiler,
            Boolean isMediaSpoiler,
            Boolean isAdult
    ) {
    }
}