package com.animap.backend.domain.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감상 기록 수정 응답")
public record RecordUpdateResponse(

        @Schema(
                description = "수정된 감상 기록의 식별자",
                example = "1"
        )
        Long recordId,

        @Schema(
                description = "감상 기록 수정 결과 메시지",
                example = "감상 기록이 수정되었습니다."
        )
        String message
) {
    public static RecordUpdateResponse of(Long recordId) {
        return new RecordUpdateResponse(recordId, "감상 기록이 수정되었습니다.");
    }
}