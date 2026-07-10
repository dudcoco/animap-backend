// 기본 정보 이외의 상세 정보
package com.animap.backend.domain.record.dto;

import com.animap.backend.domain.record.PreferenceType;
import com.animap.backend.domain.record.UserAnimeRecord;

import java.time.LocalDateTime;

public record RecordDetailResponse(
        Long recordId,
        Long animeId,
        String titleKo,
        String titleOriginal,
        String synopsis,
        String posterUrl,
        Short releaseYear,
        Integer rating,
        PreferenceType preferenceType,
        String reviewText,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static RecordDetailResponse from(UserAnimeRecord record) {
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
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}