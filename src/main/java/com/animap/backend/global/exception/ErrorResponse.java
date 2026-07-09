package com.animap.backend.global.exception;

// 에러 발생 시 클라이언트에게 보내줄 응답 형태
public record ErrorResponse(
        int status,
        String message
) {
    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message);
    }
}