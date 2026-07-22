package com.animap.backend.domain.ai;

import com.animap.backend.domain.ai.dto.ReviewAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiReviewAnalysisService {

    private final OpenAiClient openAiClient;

    public ReviewAnalysisResponse analyzeReview(String reviewText) {
        return openAiClient.analyzeReview(reviewText);
    }
}