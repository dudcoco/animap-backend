package com.animap.backend.domain.analysis;

import com.animap.backend.domain.analysis.dto.RecordAiAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordAiAnalysisController {

    private final RecordAiAnalysisService recordAiAnalysisService;

    @PostMapping("/{recordId}/ai-analysis")
    public ResponseEntity<RecordAiAnalysisResponse> analyzeRecordAndApply(
            @PathVariable Long recordId,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        RecordAiAnalysisResponse response =
                recordAiAnalysisService.analyzeRecordAndApply(userId, recordId);

        return ResponseEntity.ok(response);
    }
}