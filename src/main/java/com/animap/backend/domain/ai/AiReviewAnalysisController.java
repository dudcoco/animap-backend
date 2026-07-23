package com.animap.backend.domain.ai;

import com.animap.backend.domain.ai.dto.ReviewAnalysisApplyResponse;
import com.animap.backend.domain.ai.dto.ReviewAnalysisRequest;
import com.animap.backend.domain.ai.dto.ReviewAnalysisResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiReviewAnalysisController {

    private final AiReviewAnalysisService aiReviewAnalysisService;

    @PostMapping("/review-analysis")
    public ResponseEntity<ReviewAnalysisResponse> analyzeReview(
            @Valid @RequestBody ReviewAnalysisRequest request
    ) {
        ReviewAnalysisResponse response =
                aiReviewAnalysisService.analyzeReview(request.reviewText());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/review-analysis/apply")
    public ResponseEntity<ReviewAnalysisApplyResponse> analyzeAndApplyReview(
            Authentication authentication,
            @Valid @RequestBody ReviewAnalysisRequest request
    ) {
        Long userId = (Long) authentication.getPrincipal();

        ReviewAnalysisApplyResponse response =
                aiReviewAnalysisService.analyzeAndApplyReview(
                        userId,
                        request.reviewText()
                );

        return ResponseEntity.ok(response);
    }
}