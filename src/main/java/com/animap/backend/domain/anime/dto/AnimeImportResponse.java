package com.animap.backend.domain.anime.dto;

import com.animap.backend.domain.anime.Anime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AniList 애니메이션 가져오기 응답")
public record AnimeImportResponse(

        @Schema(
                description = "AniMap 데이터베이스에 저장된 애니메이션 식별자",
                example = "1"
        )
        Long animeId,

        @Schema(
                description = "AniList에서 사용하는 애니메이션 식별자",
                example = "20"
        )
        Integer anilistId,

        @Schema(
                description = "애니메이션 한국어 제목",
                example = "나루토"
        )
        String titleKo,

        @Schema(
                description = "애니메이션 원제",
                example = "NARUTO -ナルト-"
        )
        String titleOriginal,

        @Schema(
                description = "애니메이션 가져오기 처리 결과 메시지",
                example = "애니메이션이 저장되었습니다."
        )
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