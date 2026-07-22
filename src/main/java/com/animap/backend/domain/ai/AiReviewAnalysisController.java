package com.animap.backend.domain.ai;

import com.animap.backend.domain.ai.dto.ReviewAnalysisRequest;
import com.animap.backend.domain.ai.dto.ReviewAnalysisResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}