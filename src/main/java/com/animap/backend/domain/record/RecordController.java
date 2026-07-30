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
import com.animap.backend.domain.record.dto.RecordDetailResponse;
import com.animap.backend.domain.record.dto.RecordUpdateRequest;
import com.animap.backend.domain.record.dto.RecordUpdateResponse;
import com.animap.backend.domain.record.dto.RecordDeleteResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Tag(
        name = "감상 기록",
        description = "로그인한 사용자의 애니메이션 감상 기록을 등록, 조회, 수정, 삭제하는 API"
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @Operation(
            summary = "감상 기록 등록",
            description = "로그인한 사용자가 특정 애니메이션의 평점, 선호 상태, 감상평을 등록한다."
    )
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

    @Operation(
            summary = "내 감상 기록 목록 조회",
            description = "로그인한 사용자가 등록한 모든 감상 기록을 조회한다."
    )
    @GetMapping
    public ResponseEntity<List<RecordListResponse>> getMyRecords(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        List<RecordListResponse> response = recordService.getMyRecords(userId);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "감상 기록 상세 조회",
            description = "로그인한 사용자가 자신의 특정 감상 기록을 상세 조회한다."
    )
    @GetMapping("/{recordId}")
    public ResponseEntity<RecordDetailResponse> getMyRecordDetail(
            Authentication authentication,

            @Parameter(
                    description = "조회할 감상 기록 ID",
                    example = "1"
            )
            @PathVariable Long recordId
    ) {
        Long userId = (Long) authentication.getPrincipal();

        RecordDetailResponse response = recordService.getMyRecordDetail(userId, recordId);

        return ResponseEntity.ok(response);
    }

    // 일부 필드만 바꾸는 수정이므로 PATCH 사용
    @Operation(
            summary = "감상 기록 수정",
            description = "로그인한 사용자가 자신의 감상 기록 중 전달한 필드만 수정한다."
    )
    @PatchMapping("/{recordId}")
    public ResponseEntity<RecordUpdateResponse> updateRecord(
            Authentication authentication,
            @PathVariable Long recordId,
            @Valid @RequestBody RecordUpdateRequest request
    ) {
        Long userId = (Long) authentication.getPrincipal();

        RecordUpdateResponse response = recordService.updateRecord(userId, recordId, request);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "감상 기록 삭제",
            description = "로그인한 사용자가 자신의 감상 기록을 삭제한다."
    )
    @DeleteMapping("/{recordId}")
    public ResponseEntity<RecordDeleteResponse> deleteRecord(
            Authentication authentication,

            @Parameter(
                    description = "삭제할 감상 기록 ID",
                    example = "1"
            )
            @PathVariable Long recordId
    ) {
        Long userId = (Long) authentication.getPrincipal();

        RecordDeleteResponse response = recordService.deleteRecord(userId, recordId);

        return ResponseEntity.ok(response);
    }
}