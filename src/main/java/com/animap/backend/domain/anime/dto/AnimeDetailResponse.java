package com.animap.backend.domain.anime.dto;

import com.animap.backend.domain.anime.Anime;

import java.math.BigDecimal;
import java.util.List;

public record AnimeDetailResponse(
        Long animeId,
        Integer anilistId,
        String titleKo,
        String titleOriginal,
        String synopsis,
        String posterUrl,
        Short releaseYear,
        BigDecimal averageScore,
        String status,
        List<AnimeTagResponse> tags
) {
    public static AnimeDetailResponse of(
            Anime anime,
            List<AnimeTagResponse> tags
    ) {
        return new AnimeDetailResponse(
                anime.getId(),
                anime.getAnilistId(),
                anime.getTitleKo(),
                anime.getTitleOriginal(),
                anime.getSynopsis(),
                anime.getPosterUrl(),
                anime.getReleaseYear(),
                anime.getAverageScore(),
                anime.getStatus(),
                tags
        );
    }
}