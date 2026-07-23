package com.animap.backend.domain.ai;

import com.animap.backend.domain.ai.dto.AnalyzedTagResponse;
import com.animap.backend.domain.ai.dto.ReviewAnalysisApplyResponse;
import com.animap.backend.domain.ai.dto.ReviewAnalysisResponse;
import com.animap.backend.domain.preference.UserPreferenceTag;
import com.animap.backend.domain.preference.UserPreferenceTagRepository;
import com.animap.backend.domain.tag.Tag;
import com.animap.backend.domain.tag.TagRepository;
import com.animap.backend.domain.tag.TagType;
import com.animap.backend.domain.user.User;
import com.animap.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiReviewAnalysisService {

    private final OpenAiClient openAiClient;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final UserPreferenceTagRepository userPreferenceTagRepository;

    public ReviewAnalysisResponse analyzeReview(String reviewText) {
        return openAiClient.analyzeReview(reviewText);
    }

    @Transactional
    public ReviewAnalysisApplyResponse analyzeAndApplyReview(
            Long userId,
            String reviewText
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        ReviewAnalysisResponse analysisResult =
                openAiClient.analyzeReview(reviewText);

        for (AnalyzedTagResponse positiveTag : analysisResult.positiveTags()) {
            reflectPreferenceTag(user, positiveTag, true);
        }

        for (AnalyzedTagResponse negativeTag : analysisResult.negativeTags()) {
            reflectPreferenceTag(user, negativeTag, false);
        }

        return ReviewAnalysisApplyResponse.of(
                analysisResult.positiveTags().size(),
                analysisResult.negativeTags().size()
        );
    }

    private void reflectPreferenceTag(
            User user,
            AnalyzedTagResponse analyzedTag,
            boolean positive
    ) {
        TagType category = parseTagType(analyzedTag.category());

        Tag tag = tagRepository.findByName(analyzedTag.name())
                .orElseGet(() -> tagRepository.save(
                        Tag.create(analyzedTag.name(), category)
                ));

        BigDecimal score = BigDecimal.valueOf(analyzedTag.confidence());

        if (!positive) {
            score = score.negate();
        }

        saveOrUpdateUserPreferenceTag(user, tag, score);
    }

    private TagType parseTagType(String category) {
        try {
            return TagType.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 태그 카테고리입니다: " + category);
        }
    }

    private void saveOrUpdateUserPreferenceTag(
            User user,
            Tag tag,
            BigDecimal score
    ) {
        userPreferenceTagRepository.findByUserIdAndTagId(user.getId(), tag.getId())
                .ifPresentOrElse(
                        preferenceTag -> preferenceTag.addScore(score),
                        () -> userPreferenceTagRepository.save(
                                UserPreferenceTag.create(user, tag, score)
                        )
                );
    }
}