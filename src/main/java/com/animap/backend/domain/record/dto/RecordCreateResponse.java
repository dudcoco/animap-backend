// 등록 성공 후 클라이언트에게 몇 번 기록이 생성되었는지를 알려줌
package com.animap.backend.domain.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "감상 기록 등록 응답")
public record RecordCreateResponse(

        @Schema(
                description = "생성된 감상 기록의 식별자",
                example = "1"
        )
        Long recordId,

        @Schema(
                description = "감상 기록 등록 결과 메시지",
                example = "감상 기록이 등록되었습니다."
        )
        String message
) {
    public static RecordCreateResponse of(Long recordId) {
        return new RecordCreateResponse(recordId, "감상 기록이 등록되었습니다.");
    }
}