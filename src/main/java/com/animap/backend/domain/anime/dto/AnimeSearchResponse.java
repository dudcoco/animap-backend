// 검색 결과 목록용 DTO
package com.animap.backend.domain.anime.dto;

import com.animap.backend.domain.anime.Anime;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "저장된 애니메이션 검색 결과")
public record AnimeSearchResponse(

        @Schema(
                description = "AniMap 데이터베이스의 애니메이션 식별자",
                example = "1"
        )
        Long animeId,

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
                description = "애니메이션 포스터 이미지 URL",
                example = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/example.jpg"
        )
        String posterUrl,

        @Schema(
                description = "애니메이션 방영 시작 연도",
                example = "2002"
        )
        Short releaseYear,

        @Schema(
                description = "애니메이션 방영 상태",
                example = "FINISHED"
        )
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