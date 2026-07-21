package com.animap.backend.domain.recommendation;

import com.animap.backend.domain.recommendation.dto.RecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<List<RecommendationResponse>> getRecommendations(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        List<RecommendationResponse> response = recommendationService.getRecommendations(userId);

        return ResponseEntity.ok(response);
    }
}