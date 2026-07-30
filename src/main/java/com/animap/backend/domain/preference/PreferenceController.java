package com.animap.backend.domain.preference;

import com.animap.backend.domain.preference.dto.PreferenceAnalyzeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.animap.backend.domain.preference.dto.PreferenceTagResponse;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "취향 분석",
        description = "사용자의 감상 기록을 기반으로 선호 태그를 분석하고 조회하는 API"
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    @Operation(
            summary = "내 취향 분석",
            description = """
                    로그인한 사용자의 감상 기록과 AI 분석 결과를 기반으로
                    사용자 선호 태그와 선호 점수를 다시 계산하여 저장한다.
                    """
    )
    @PostMapping("/analyze")
    public ResponseEntity<PreferenceAnalyzeResponse> analyzeMyPreferences(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        PreferenceAnalyzeResponse response = preferenceService.analyzeMyPreferences(userId);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "내 선호 태그 조회",
            description = "로그인한 사용자의 분석된 선호 태그와 태그별 선호 점수를 조회한다."
    )
    @GetMapping
    public ResponseEntity<List<PreferenceTagResponse>> getMyPreferenceTags(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        List<PreferenceTagResponse> response = preferenceService.getMyPreferenceTags(userId);

        return ResponseEntity.ok(response);
    }
}