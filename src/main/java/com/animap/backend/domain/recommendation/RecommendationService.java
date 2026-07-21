package com.animap.backend.domain.recommendation;

import com.animap.backend.domain.anime.Anime;
import com.animap.backend.domain.recommendation.dto.RecommendationResponse;
import com.animap.backend.domain.preference.UserPreferenceTag;
import com.animap.backend.domain.preference.UserPreferenceTagRepository;
import com.animap.backend.domain.record.UserAnimeRecordRepository;
import com.animap.backend.domain.tag.AnimeTag;
import com.animap.backend.domain.tag.AnimeTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    private final UserPreferenceTagRepository userPreferenceTagRepository;
    private final AnimeTagRepository animeTagRepository;
    private final UserAnimeRecordRepository recordRepository;

    public List<RecommendationResponse> getRecommendations(Long userId) {
        List<UserPreferenceTag> preferenceTags = userPreferenceTagRepository.findAllByUserId(userId);

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

        return candidateMap.values()
                .stream()
                .sorted(Comparator.comparing(RecommendationCandidate::getScore).reversed())
                .map(RecommendationCandidate::toResponse)
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

        private RecommendationResponse toResponse() {
            List<String> tags = new ArrayList<>(matchedTags);

            String reason = String.join(", ", tags) + " 태그가 사용자 취향과 일치합니다.";

            return new RecommendationResponse(
                    anime.getId(),
                    anime.getTitleKo(),
                    anime.getTitleOriginal(),
                    anime.getPosterUrl(),
                    anime.getReleaseYear(),
                    score,
                    tags,
                    reason
            );
        }
    }
}