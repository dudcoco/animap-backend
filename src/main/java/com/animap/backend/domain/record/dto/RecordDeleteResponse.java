package com.animap.backend.domain.record.dto;

public record RecordDeleteResponse(
        Long recordId,
        String message
) {
    public static RecordDeleteResponse of(Long recordId) {
        return new RecordDeleteResponse(recordId, "감상 기록이 삭제되었습니다.");
    }
}