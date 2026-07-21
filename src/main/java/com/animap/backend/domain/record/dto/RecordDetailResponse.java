package com.animap.backend.domain.record.dto;

import com.animap.backend.domain.anime.dto.AnimeTagResponse;
import com.animap.backend.domain.record.PreferenceType;
import com.animap.backend.domain.record.UserAnimeRecord;

import java.time.LocalDateTime;
import java.util.List;

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
        List<AnimeTagResponse> tags,
        LocalDateTime createdAt,
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