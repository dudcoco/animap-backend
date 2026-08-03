package com.animap.backend.domain.ai;

import com.animap.backend.domain.ai.dto.ReviewAnalysisApplyResponse;
import com.animap.backend.domain.ai.dto.ReviewAnalysisRequest;
import com.animap.backend.domain.ai.dto.ReviewAnalysisResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "AI 리뷰 분석",
        description = "사용자가 작성한 감상평을 AI로 분석하고 분석 결과를 사용자 선호도에 반영하는 API"
)
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiReviewAnalysisController {

    private final AiReviewAnalysisService aiReviewAnalysisService;

    @Operation(
            summary = "감상평 AI 분석 (테스트용으로 생성, 실사용 X)",
            description = """
                    입력한 감상평을 AI로 분석하여
                    긍정 태그, 부정 태그와 분석 요약을 반환한다.
                    분석 결과는 사용자 선호 태그에 저장하지 않는다.
                    """
    )
    @PostMapping("/review-analysis")
    public ResponseEntity<ReviewAnalysisResponse> analyzeReview(
            @Valid @RequestBody ReviewAnalysisRequest request
    ) {
        ReviewAnalysisResponse response =
                aiReviewAnalysisService.analyzeReview(request.reviewText());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "감상평 AI 분석 및 선호도 반영",
            description = """
                    로그인한 사용자가 입력한 감상평을 AI로 분석하고,
                    분석된 긍정 및 부정 태그를 사용자 선호 태그에 반영한다.
                    """
    )
    @SecurityRequirement(name = "Bearer Autentication")
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