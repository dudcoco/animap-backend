package com.animap.backend.domain.anime.dto;

public record AnimeBulkImportResponse(
        int requestedCount,
        int savedCount,
        int skippedCount,
        String message
) {
    public static AnimeBulkImportResponse of(
            int requestedCount,
            int savedCount,
            int skippedCount
    ) {
        return new AnimeBulkImportResponse(
                requestedCount,
                savedCount,
                skippedCount,
                "인기 애니메이션 가져오기가 완료되었습니다."
        );
    }
}