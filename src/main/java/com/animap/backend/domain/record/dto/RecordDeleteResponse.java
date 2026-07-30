package com.animap.backend.domain.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감상 기록 삭제 응답")
public record RecordDeleteResponse(

        @Schema(
                description = "삭제된 감상 기록의 식별자",
                example = "1"
        )
        Long recordId,
        
        @Schema(
                description = "감상 기록 삭제 결과 메시지",
                example = "감상 기록이 삭제되었습니다."
        )
        String message
) {
    public static RecordDeleteResponse of(Long recordId) {
        return new RecordDeleteResponse(recordId, "감상 기록이 삭제되었습니다.");
    }
}