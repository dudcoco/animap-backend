package com.animap.backend.domain.anime.dto;

public record ExternalAnimeSearchResponse(
        Integer anilistId,
        String titleKo,
        String titleOriginal,
        String titleRomaji,
        String synopsis,
        String posterUrl,
        Short releaseYear,
        Double averageScore,
        String status
) {
}