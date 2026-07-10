package com.animap.backend.domain.record.dto;

import com.animap.backend.domain.record.PreferenceType;
import com.animap.backend.domain.record.UserAnimeRecord;

public record RecordListResponse(
        Long recordId,
        Long animeId,
        String titleKo,
        String titleOriginal,
        String posterUrl,
        Integer rating,
        PreferenceType preferenceType,
        String reviewText
) {
    public static RecordListResponse from(UserAnimeRecord record) {
        return new RecordListResponse(
                record.getId(),
                record.getAnime().getId(),
                record.getAnime().getTitleKo(),
                record.getAnime().getTitleOriginal(),
                record.getAnime().getPosterUrl(),
                record.getRating(),
                record.getPreferenceType(),
                record.getReviewText()
        );
    }
}