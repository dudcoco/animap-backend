package com.animap.backend.domain.recommendation;

import com.animap.backend.domain.recommendation.dto.RecommendationListResponse;
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
    public ResponseEntity<RecommendationListResponse> getRecommendations(
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();

        List<RecommendationResponse> recommendations =
                recommendationService.getRecommendations(userId);

        RecommendationListResponse response =
                RecommendationListResponse.of(recommendations);

        return ResponseEntity.ok(response);
    }
}