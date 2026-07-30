package com.animap.backend.domain.recommendation;

import com.animap.backend.domain.recommendation.dto.RecommendationListResponse;
import com.animap.backend.domain.recommendation.dto.RecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "애니메이션 추천",
        description = "사용자의 선호 태그를 기반으로 애니메이션 추천 결과를 조회하는 API"
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Operation(
            summary = "내 애니메이션 추천 조회",
            description = """
                    로그인한 사용자의 선호 태그와 애니메이션 태그를 비교하여
                    추천 점수, 추천 이유, 주의 이유가 포함된 애니메이션 목록을 조회한다.
                    """
    )
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