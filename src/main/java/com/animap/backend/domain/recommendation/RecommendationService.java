package com.animap.backend.domain.recommendation;

import com.animap.backend.domain.anime.Anime;
import com.animap.backend.domain.preference.UserPreferenceTag;
import com.animap.backend.domain.preference.UserPreferenceTagRepository;
import com.animap.backend.domain.recommendation.dto.RecommendationResponse;
import com.animap.backend.domain.record.UserAnimeRecordRepository;
import com.animap.backend.domain.tag.AnimeTag;
import com.animap.backend.domain.tag.AnimeTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    private final UserPreferenceTagRepository userPreferenceTagRepository;
    private final AnimeTagRepository animeTagRepository;
    private final UserAnimeRecordRepository recordRepository;

    public List<RecommendationResponse> getRecommendations(Long userId) {
        List<UserPreferenceTag> preferenceTags =
                userPreferenceTagRepository.findAllByUserId(userId);

        Map<Long, RecommendationCandidate> candidateMap = new HashMap<>();

        for (UserPreferenceTag preferenceTag : preferenceTags) {
            if (preferenceTag.getPreferenceScore().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            List<AnimeTag> animeTags = animeTagRepository.findAllByTag_Id(
                    preferenceTag.getTag().getId()
            );

            for (AnimeTag animeTag : animeTags) {
                Anime anime = animeTag.getAnime();

                if (recordRepository.existsByUserIdAndAnimeId(userId, anime.getId())) {
                    continue;
                }

                // 실제 추천 로직의 핵심 점수
                // 이 애니가 사용자 취향 태그와 얼마나 맞는지를 계산한 내부점수
                BigDecimal score = preferenceTag.getPreferenceScore()
                        .multiply(BigDecimal.valueOf(animeTag.getWeight()));

                RecommendationCandidate candidate = candidateMap.computeIfAbsent(
                        anime.getId(),
                        animeId -> new RecommendationCandidate(anime)
                );

                candidate.addScore(score);
                candidate.addMatchedTag(preferenceTag.getTag().getName());
            }
        }

        List<RecommendationCandidate> candidates = candidateMap.values()
                .stream()
                .sorted(Comparator.comparing(RecommendationCandidate::getScore).reversed())
                .toList();

        if (candidates.isEmpty()) {
            return List.of();
        }

        BigDecimal maxScore = candidates.get(0).getScore();

        return candidates.stream()
                .map(candidate -> candidate.toResponse(maxScore))
                .toList();
    }

    private static class RecommendationCandidate {

        private final Anime anime;
        private BigDecimal score = BigDecimal.ZERO;
        private final Set<String> matchedTags = new LinkedHashSet<>();

        private RecommendationCandidate(Anime anime) {
            this.anime = anime;
        }

        private void addScore(BigDecimal scoreToAdd) {
            this.score = this.score.add(scoreToAdd);
        }

        private void addMatchedTag(String tagName) {
            this.matchedTags.add(tagName);
        }

        private BigDecimal getScore() {
            return score;
        }

        private RecommendationResponse toResponse(BigDecimal maxScore) {
            List<String> tags = new ArrayList<>(matchedTags);

            // 내부 계산값을 응답에 그대로 보여주는 값
            BigDecimal rawScore = score.setScale(2, RoundingMode.HALF_UP);

            // 현재 추천 후보 중 최고 점수를 기준으로 환산한 값
            BigDecimal matchPercent = score
                    .multiply(BigDecimal.valueOf(100))
                    .divide(maxScore, 2, RoundingMode.HALF_UP);

            String reason = createReason(tags);

            return new RecommendationResponse(
                    anime.getId(),
                    anime.getTitleKo(),
                    anime.getTitleOriginal(),
                    anime.getPosterUrl(),
                    anime.getReleaseYear(),
                    rawScore,
                    matchPercent,
                    tags,
                    reason
            );
        }
        private String createReason(List<String> tags) {
            if (tags.isEmpty()) {
                return "사용자 취향과 유사한 요소가 있어 추천합니다.";
            }

            String tagText = String.join(", ", tags);

            return tagText + " 요소가 사용자 취향과 잘 맞아 추천합니다.";
        }
    }
}