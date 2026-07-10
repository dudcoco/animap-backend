package com.animap.backend.domain.anime.dto;

import com.animap.backend.domain.anime.Anime;

import java.math.BigDecimal;

public record AnimeDetailResponse(
        Long animeId,
        Integer anilistId,
        String titleKo,
        String titleOriginal,
        String synopsis,
        String posterUrl,
        Short releaseYear,
        BigDecimal averageScore,
        String status
) {
    public static AnimeDetailResponse from(Anime anime) {
        return new AnimeDetailResponse(
                anime.getId(),
                anime.getAnilistId(),
                anime.getTitleKo(),
                anime.getTitleOriginal(),
                anime.getSynopsis(),
                anime.getPosterUrl(),
                anime.getReleaseYear(),
                anime.getAverageScore(),
                anime.getStatus()
        );
    }
}