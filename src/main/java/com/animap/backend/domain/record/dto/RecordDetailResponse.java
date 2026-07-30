package com.animap.backend.domain.record.dto;

import com.animap.backend.domain.anime.dto.AnimeTagResponse;
import com.animap.backend.domain.record.PreferenceType;
import com.animap.backend.domain.record.UserAnimeRecord;

import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감상 기록 상세 조회 응답")
public record RecordDetailResponse(

        @Schema(
                description = "감상 기록의 식별자",
                example = "1"
        )
        Long recordId,

        @Schema(
                description = "감상 기록과 연결된 애니메이션의 식별자",
                example = "20"
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
                description = "애니메이션 줄거리",
                example = "마을에서 인정받지 못하던 닌자 나루토가 호카게를 꿈꾸며 성장하는 이야기"
        )
        String synopsis,

        @Schema(
                description = "애니메이션 포스터 이미지 URL",
                example = "https://example.com/posters/naruto.jpg"
        )
        String posterUrl,

        @Schema(
                description = "애니메이션 방영 연도",
                example = "2002"
        )
        Short releaseYear,

        @Schema(
                description = "사용자가 매긴 평점. 입력하지 않은 경우 null",
                example = "9",
                minimum = "1",
                maximum = "10",
                nullable = true
        )
        Integer rating,

        @Schema(
                description = "애니메이션에 대한 사용자의 선호 상태",
                example = "LIKE"
        )
        PreferenceType preferenceType,

        @Schema(
                description = "사용자가 작성한 감상평. 입력하지 않은 경우 null",
                example = "긴장감 있는 전개와 캐릭터의 성장 과정이 좋았다.",
                nullable = true
        )
        String reviewText,

        @Schema(
                description = "애니메이션에 등록된 태그 목록"
        )
        List<AnimeTagResponse> tags,

        @Schema(
                description = "감상 기록 생성 일시",
                example = "2026-07-30T02:30:00"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "감상 기록 최종 수정 일시",
                example = "2026-07-30T02:40:00"
        )
        LocalDateTime updatedAt
) {
    public static RecordDetailResponse of(
            UserAnimeRecord record,
            List<AnimeTagResponse> tags
    ) {
        return new RecordDetailResponse(
                record.getId(),
                record.getAnime().getId(),
                record.getAnime().getTitleKo(),
                record.getAnime().getTitleOriginal(),
                record.getAnime().getSynopsis(),
                record.getAnime().getPosterUrl(),
                record.getAnime().getReleaseYear(),
                record.getRating(),
                record.getPreferenceType(),
                record.getReviewText(),
                tags,
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}