package com.animap.backend.domain.analysis;

import com.animap.backend.domain.analysis.dto.RecordAiAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "AI 감상 분석",
        description = "사용자의 감상 기록을 AI로 분석하여 선호 태그를 추출하는 API"
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordAiAnalysisController {

    private final RecordAiAnalysisService recordAiAnalysisService;

    @Operation(
            summary = "감상 기록 AI 분석",
            description = """
                    로그인한 사용자의 특정 감상 기록을 AI로 분석한다.
                    감상평과 선호 정보를 바탕으로 관련 태그, 감정 방향, 신뢰도를 추출하고 분석 결과를 저장한다.
                    """
    )
    @PostMapping("/{recordId}/ai-analysis")
    public ResponseEntity<RecordAiAnalysisResponse> analyzeRecordAndApply(

            @Parameter(
                    description = "AI 분석을 실행할 감상 기록 ID",
                    example = "1"
            )
            @PathVariable Long recordId,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        RecordAiAnalysisResponse response =
                recordAiAnalysisService.analyzeRecordAndApply(userId, recordId);

        return ResponseEntity.ok(response);
    }
}