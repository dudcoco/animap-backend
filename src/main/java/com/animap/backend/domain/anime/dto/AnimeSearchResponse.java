// 검색 결과 목록용 DTO
package com.animap.backend.domain.anime.dto;

import com.animap.backend.domain.anime.Anime;

public record AnimeSearchResponse(
        Long animeId,
        String titleKo,
        String titleOriginal,
        String posterUrl,
        Short releaseYear,
        String status
) {
    public static AnimeSearchResponse from(Anime anime) {
        return new AnimeSearchResponse(
                anime.getId(),
                anime.getTitleKo(),
                anime.getTitleOriginal(),
                anime.getPosterUrl(),
                anime.getReleaseYear(),
                anime.getStatus()
        );
    }
}