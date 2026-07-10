package com.animap.backend.domain.record.dto;

public record RecordUpdateResponse(
        Long recordId,
        String message
) {
    public static RecordUpdateResponse of(Long recordId) {
        return new RecordUpdateResponse(recordId, "감상 기록이 수정되었습니다.");
    }
}