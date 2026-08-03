package com.animap.backend.domain.anime.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AniList 외부 애니메이션 검색 결과")
public record ExternalAnimeSearchResponse(

        @Schema(
                description = "AniList 애니메이션 식별자",
                example = "20"
        )
        Integer anilistId,

        @Schema(
                description = "애니메이션 한국어 제목",
                example = "나루토"
        )
        String titleKo,

        @Schema(
                description = "애니메이션 영어 제목",
                example = "Naruto"
        )
        String titleEnglish,

        @Schema(
                description = "애니메이션 로마자 제목",
                example = "Naruto"
        )
        String titleRomaji,

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
                description = "AniList 사용자 평점 평균",
                example = "79.0"
        )
        Double averageScore,

        @Schema(
                description = "애니메이션 방영 상태",
                example = "FINISHED"
        )
        String status,

        @Schema(
                description = "AniList에서 제공하는 장르 목록",
                example = "[\"Action\", \"Adventure\", \"Comedy\"]"
        )
        List<String> genres,

        @Schema(
                description = "AniList에서 제공하는 태그 이름 목록",
                example = "[\"Ninja\", \"Coming of Age\", \"Shounen\"]"
        )
        List<String> tagNames
) {
}