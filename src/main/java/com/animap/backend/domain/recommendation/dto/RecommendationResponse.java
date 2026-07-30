package com.animap.backend.domain.recommendation.dto;

import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "애니메이션 추천 결과")
public record RecommendationResponse(

        @Schema(
                description = "추천 애니메이션의 식별자",
                example = "12"
        )
        Long animeId,

        @Schema(
                description = "애니메이션 한국어 제목",
                example = "나루토"
        )
        String titleKo,

        @Schema(
                description = "애니메이션 원제",
                example = "NARUTO -ナルト-"
        )
        String titleOriginal,

        @Schema(
                description = "애니메이션 포스터 이미지 URL",
                example = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/example.jpg"
        )
        String posterUrl,

        @Schema(
                description = "애니메이션 방영 연도",
                example = "2002"
        )
        Short releaseYear,

        @Schema(
                description = "추천 로직이 사용자 선호 태그와 애니메이션 태그를 비교하여 계산한 내부 원점수",
                example = "24.50"
        )
        BigDecimal rawScore, // 추천 로직이 실제로 계산한 내부 점수

        @Schema(
                description = "현재 추천 후보 중 최고 원점수를 100으로 환산한 상대 적합도",
                example = "87.50",
                minimum = "0",
                maximum = "100"
        )
        BigDecimal matchPercent, // 현재 추천후보 중 최고점수를 100으로 보고 환산한 상대 적합도

        @Schema(
                description = "사용자의 긍정적 선호와 일치한 태그 이름 목록",
                example = "[\"성장\", \"우정\", \"액션\"]"
        )
        List<String> matchedTags,

        @Schema(
                description = "사용자의 부정적 선호와 관련된 주의 태그 이름 목록",
                example = "[\"폭력성\", \"잔혹성\"]"
        )
        List<String> cautionTags,

        @Schema(
                description = "사용자의 선호 태그를 기반으로 생성한 추천 이유",
                example = "선호하는 성장, 우정, 액션 태그가 포함되어 있습니다."
        )
        String reason,

        @Schema(
                description = "주의 태그를 기반으로 생성한 안내 메시지. 주의 태그가 없으면 null일 수 있음",
                example = "폭력성과 잔혹성 요소가 포함되어 있어 주의가 필요합니다.",
                nullable = true
        )
        String cautionMessage
) {
}