package com.animap.backend.domain.preference;

import com.animap.backend.domain.preference.dto.PreferenceAnalyzeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    @PostMapping("/analyze")
    public ResponseEntity<PreferenceAnalyzeResponse> analyzeMyPreferences(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        PreferenceAnalyzeResponse response = preferenceService.analyzeMyPreferences(userId);

        return ResponseEntity.ok(response);
    }
}