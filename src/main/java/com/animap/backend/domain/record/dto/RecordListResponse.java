package com.animap.backend.domain.record.dto;

import com.animap.backend.domain.anime.dto.AnimeTagResponse;
import com.animap.backend.domain.record.PreferenceType;
import com.animap.backend.domain.record.UserAnimeRecord;

import java.util.List;

public record RecordListResponse(
        Long recordId,
        Long animeId,
        String titleKo,
        String titleOriginal,
        String posterUrl,
        Integer rating,
        PreferenceType preferenceType,
        String reviewText,
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