package com.animap.backend.domain.anime.dto;

import com.animap.backend.domain.anime.Anime;

import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "애니메이션 상세 조회 응답")
public record AnimeDetailResponse(

        @Schema(
                description = "AniMap 데이터베이스의 애니메이션 식별자",
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
                description = "애니메이션 줄거리",
                example = "호카게를 꿈꾸는 소년 닌자가 동료들과 함께 성장하는 이야기"
        )
        String synopsis,

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
                description = "AniList 사용자 평균 점수. 100점 만점",
                example = "79.00"
        )
        BigDecimal averageScore,

        @Schema(
                description = "애니메이션 방영 상태",
                example = "FINISHED"
        )
        String status,

        @Schema(
                description = "애니메이션에 연결된 표준 태그 목록"
        )
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