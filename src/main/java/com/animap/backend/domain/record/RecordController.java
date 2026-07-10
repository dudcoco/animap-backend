package com.animap.backend.domain.record;

import com.animap.backend.domain.record.dto.RecordCreateRequest;
import com.animap.backend.domain.record.dto.RecordCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.animap.backend.domain.record.dto.RecordListResponse;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    public ResponseEntity<RecordCreateResponse> createRecord(
            Authentication authentication,
            @Valid @RequestBody RecordCreateRequest request
    ) {
        // JwtAuthenticationFilter에서 토큰을 검증한 뒤 넣어둔 값
        Long userId = (Long) authentication.getPrincipal();

        RecordCreateResponse response = recordService.createRecord(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<RecordListResponse>> getMyRecords(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        List<RecordListResponse> response = recordService.getMyRecords(userId);

        return ResponseEntity.ok(response);
    }
}