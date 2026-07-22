package com.animap.backend.domain.ai;

import com.animap.backend.domain.ai.dto.AnalyzedTagResponse;
import com.animap.backend.domain.ai.dto.ReviewAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private static final String OPENAI_RESPONSES_URL = "https://api.openai.com/v1/responses";

    private final RestClient.Builder restClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    public ReviewAnalysisResponse analyzeReview(String reviewText) {
        RestClient restClient = restClientBuilder.build();

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "input", createPrompt(reviewText)
        );

        String responseBody = restClient.post()
                .uri(OPENAI_RESPONSES_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiKey)
                .body(requestBody)
                .retrieve()
                .body(String.class);

        String outputText = extractOutputText(responseBody);

        return parseAnalysisResult(outputText);
    }

    private String createPrompt(String reviewText) {
        return """
                너는 애니메이션 감상문에서 사용자의 선호/비선호 요소를 추출하는 분석기다.

                아래 감상문을 읽고, 사용자가 좋아한 요소와 싫어한 요소를 태그로 추출해라.

                반드시 아래 JSON 형식으로만 응답해라.
                설명 문장, 마크다운, 코드블록은 절대 포함하지 마라.

                사용 가능한 category:
                - GENRE: 장르. 예: 액션, 스포츠, 판타지, 로맨스
                - MOOD: 분위기. 예: 밝음, 어두움, 잔잔함, 긴장감
                - THEME: 소재/주제. 예: 성장, 우정, 복수, 가족
                - WARNING: 주의 요소. 예: 폭력성, 고어, 우울한 전개
                - ART_STYLE: 그림체/작화. 예: 그림체가 투박함, 작화가 화려함
                - PACING: 전개 속도. 예: 전개가 느림, 급전개

                confidence는 1부터 5 사이 숫자로 넣어라.
                감상문에 직접 드러나지 않은 요소는 추측하지 마라.

                응답 형식:
                {
                  "positiveTags": [
                    {
                      "name": "성장",
                      "category": "THEME",
                      "confidence": 5
                    }
                  ],
                  "negativeTags": [
                    {
                      "name": "폭력성",
                      "category": "WARNING",
                      "confidence": 4
                    }
                  ],
                  "summary": "사용자는 성장 요소를 긍정적으로 보고, 폭력적인 장면은 부정적으로 느꼈다."
                }

                감상문:
                %s
                """.formatted(reviewText);
    }

    private String extractOutputText(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);

            JsonNode output = root.get("output");
            if (output == null || !output.isArray()) {
                throw new IllegalArgumentException("AI 응답에서 output을 찾을 수 없습니다.");
            }

            for (JsonNode outputItem : output) {
                JsonNode content = outputItem.get("content");

                if (content == null || !content.isArray()) {
                    continue;
                }

                for (JsonNode contentItem : content) {
                    JsonNode textNode = contentItem.get("text");

                    if (textNode != null && !textNode.asText().isBlank()) {
                        return textNode.asText();
                    }
                }
            }

            throw new IllegalArgumentException("AI 응답에서 text를 찾을 수 없습니다.");

        } catch (Exception e) {
            throw new IllegalArgumentException("AI 응답을 해석하는 중 오류가 발생했습니다.");
        }
    }

    private ReviewAnalysisResponse parseAnalysisResult(String outputText) {
        try {
            JsonNode root = objectMapper.readTree(outputText);

            List<AnalyzedTagResponse> positiveTags =
                    parseTags(root.get("positiveTags"));

            List<AnalyzedTagResponse> negativeTags =
                    parseTags(root.get("negativeTags"));

            String summary = root.get("summary") != null
                    ? root.get("summary").asText()
                    : "";

            return new ReviewAnalysisResponse(
                    positiveTags,
                    negativeTags,
                    summary
            );

        } catch (Exception e) {
            throw new IllegalArgumentException("AI 분석 결과를 JSON으로 변환하지 못했습니다.");
        }
    }

    private List<AnalyzedTagResponse> parseTags(JsonNode tagNodes) {
        List<AnalyzedTagResponse> tags = new ArrayList<>();

        if (tagNodes == null || !tagNodes.isArray()) {
            return tags;
        }

        for (JsonNode tagNode : tagNodes) {
            String name = tagNode.get("name").asText();
            String category = tagNode.get("category").asText();
            int confidence = tagNode.get("confidence").asInt();

            tags.add(new AnalyzedTagResponse(
                    name,
                    category,
                    confidence
            ));
        }

        return tags;
    }
}