package com.animap.backend.domain.ai;

import com.animap.backend.domain.ai.dto.ReviewAnalysisResponse;
import com.animap.backend.domain.tag.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private static final String OPENAI_RESPONSES_API_URL = "https://api.openai.com/v1/responses";

    private final RestClient.Builder restClientBuilder;
    private final ObjectMapper objectMapper;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.model}")
    private String model;

    public ReviewAnalysisResponse analyzeReview(
            String reviewText,
            List<Tag> availableTags
    ) {
        RestClient restClient = restClientBuilder.build();

        String prompt = buildPrompt(reviewText, availableTags);

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "input", prompt
        );

        String responseBody = restClient.post()
                .uri(OPENAI_RESPONSES_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiKey)
                .body(requestBody)
                .retrieve()
                .body(String.class);

        String outputText = extractOutputText(responseBody);

        return parseReviewAnalysisResponse(outputText);
    }

    private String buildPrompt(
            String reviewText,
            List<Tag> availableTags
    ) {
        String availableTagText = formatAvailableTags(availableTags);

        return """
                당신은 애니메이션 감상문을 분석하여 사용자의 선호/비선호 요소를 추출하는 AI입니다.

                아래 [사용 가능한 태그 목록] 중에서만 태그를 선택해야 합니다.
                절대로 새로운 태그 이름을 만들지 마세요.
                태그 이름은 반드시 목록에 있는 name 값을 그대로 사용하세요.
                category 값도 반드시 목록에 있는 category 값을 그대로 사용하세요.

                예를 들어 목록에 '폭력성 / WARNING'이 있다면,
                '폭력', '잔혹한 장면', '폭력적인 요소'처럼 바꿔 쓰지 말고 반드시 '폭력성'이라고 출력해야 합니다.

                사용자가 좋아한 요소는 positiveTags에 넣고,
                사용자가 싫어한 요소는 negativeTags에 넣으세요.

                confidence는 1부터 5 사이의 정수입니다.
                감상문에서 강하게 드러난 요소일수록 높은 값을 주세요.

                감상문과 관련 있는 태그가 없다면 빈 배열을 반환하세요.

                응답은 반드시 아래 JSON 형식만 반환하세요.
                설명 문장이나 마크다운 코드블록은 넣지 마세요.

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
                  "summary": "사용자는 캐릭터 성장 요소를 긍정적으로 느꼈고, 폭력적인 장면은 부정적으로 느꼈다."
                }

                [사용 가능한 태그 목록]
                %s

                [감상문]
                %s
                """.formatted(
                availableTagText,
                reviewText
        );
    }

    private String formatAvailableTags(List<Tag> availableTags) {
        if (availableTags == null || availableTags.isEmpty()) {
            throw new IllegalArgumentException("AI 분석에 사용할 표준 태그 목록이 없습니다.");
        }

        StringBuilder builder = new StringBuilder();

        for (Tag tag : availableTags) {
            builder.append("- ")
                    .append(tag.getName())
                    .append(" / ")
                    .append(tag.getCategory().name())
                    .append("\n");
        }

        return builder.toString();
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

    private ReviewAnalysisResponse parseReviewAnalysisResponse(String outputText) {
        try {
            String cleanedOutputText = removeMarkdownCodeBlock(outputText);

            return objectMapper.readValue(
                    cleanedOutputText,
                    ReviewAnalysisResponse.class
            );

        } catch (Exception e) {
            throw new IllegalArgumentException("AI 분석 결과를 JSON으로 변환하는 중 오류가 발생했습니다.");
        }
    }

    private String removeMarkdownCodeBlock(String text) {
        if (text == null) {
            return "";
        }

        String trimmedText = text.trim();

        if (trimmedText.startsWith("```json")) {
            return trimmedText
                    .replaceFirst("```json", "")
                    .replaceFirst("```", "")
                    .trim();
        }

        if (trimmedText.startsWith("```")) {
            return trimmedText
                    .replaceFirst("```", "")
                    .replaceFirst("```", "")
                    .trim();
        }

        return trimmedText;
    }
}