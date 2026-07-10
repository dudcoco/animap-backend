// 등록 성공 후 클라이언트에게 몇 번 기록이 생성되었는지를 알려줌
package com.animap.backend.domain.record.dto;

public record RecordCreateResponse(
        Long recordId,
        String message
) {
    public static RecordCreateResponse of(Long recordId) {
        return new RecordCreateResponse(recordId, "감상 기록이 등록되었습니다.");
    }
}