package com.animap.backend.domain.record.dto;

import com.animap.backend.domain.anime.dto.AnimeTagResponse;
import com.animap.backend.domain.record.PreferenceType;
import com.animap.backend.domain.record.UserAnimeRecord;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감상 기록 목록 조회 응답")
public record RecordListResponse(
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
                description = "애니메이션 포스터 이미지 URL",
                example = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/example.jpg"
        )
        String posterUrl,

        @Schema(
                description = "사용자가 매긴 평점. 입력하지 않은 경우 null",
                example = "8",
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
                example = "캐릭터의 성장 과정과 전투 장면이 좋았다.",
                nullable = true
        )
        String reviewText,

        @Schema(
                description = "애니메이션에 등록된 태그 목록"
        )
        List<AnimeTagResponse> tags
) {
    public static RecordListResponse of(
            UserAnimeRecord record,
            List<AnimeTagResponse> tags
    ) {
        return new RecordListResponse(
                record.getId(),
                record.getAnime().getId(),
                record.getAnime().getTitleKo(),
                record.getAnime().getTitleOriginal(),
                record.getAnime().getPosterUrl(),
                record.getRating(),
                record.getPreferenceType(),
                record.getReviewText(),
                tags
        );
    }
}