package com.animap.backend.domain.anime.dto;

import java.util.List;

public record ExternalAnimeSearchResponse(
        Integer anilistId,
        String titleKo,
        String titleEnglish,
        String titleRomaji,
        String synopsis,
        String posterUrl,
        Short releaseYear,
        Double averageScore,
        String status,
        List<String> genres,
        List<String> tagNames
) {
}