package com.animap.backend.domain.anime.dto;

import com.animap.backend.domain.anime.Anime;

public record AnimeImportResponse(
        Long animeId,
        Integer anilistId,
        String titleKo,
        String titleOriginal,
        String message
) {
    public static AnimeImportResponse saved(Anime anime) {
        return new AnimeImportResponse(
                anime.getId(),
                anime.getAnilistId(),
                anime.getTitleKo(),
                anime.getTitleOriginal(),
                "애니메이션이 저장되었습니다."
        );
    }

    public static AnimeImportResponse alreadyExists(Anime anime) {
        return new AnimeImportResponse(
                anime.getId(),
                anime.getAnilistId(),
                anime.getTitleKo(),
                anime.getTitleOriginal(),
                "이미 저장된 애니메이션입니다."
        );
    }
}