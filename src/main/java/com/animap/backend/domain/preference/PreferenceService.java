package com.animap.backend.domain.preference;

import com.animap.backend.domain.preference.dto.PreferenceAnalyzeResponse;
import com.animap.backend.domain.record.PreferenceType;
import com.animap.backend.domain.record.UserAnimeRecord;
import com.animap.backend.domain.record.UserAnimeRecordRepository;
import com.animap.backend.domain.tag.AnimeTag;
import com.animap.backend.domain.tag.AnimeTagRepository;
import com.animap.backend.domain.tag.Tag;
import com.animap.backend.domain.user.User;
import com.animap.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.animap.backend.domain.preference.dto.PreferenceTagResponse;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PreferenceService {

    private final UserRepository userRepository;
    private final UserAnimeRecordRepository recordRepository;
    private final AnimeTagRepository animeTagRepository;
    private final UserPreferenceTagRepository userPreferenceTagRepository;

    @Transactional
    public PreferenceAnalyzeResponse analyzeMyPreferences(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<UserAnimeRecord> records = recordRepository.findAllByUserId(userId);

        int updatedPreferenceTagCount = 0;

        for (UserAnimeRecord record : records) {
            List<AnimeTag> animeTags = animeTagRepository.findAllByAnime_Id(
                    record.getAnime().getId()
            );

            for (AnimeTag animeTag : animeTags) {
                BigDecimal score = calculateScore(record.getPreferenceType(), animeTag.getWeight());

                saveOrUpdatePreferenceTag(user, animeTag.getTag(), score);

                updatedPreferenceTagCount++;
            }
        }

        return PreferenceAnalyzeResponse.of(
                records.size(),
                updatedPreferenceTagCount
        );
    }

    @Transactional(readOnly = true)
    public List<PreferenceTagResponse> getMyPreferenceTags(Long userId) {
        return userPreferenceTagRepository.findAllByUserId(userId)
                .stream()
                .map(PreferenceTagResponse::from)
                .toList();
    }

    private BigDecimal calculateScore(PreferenceType preferenceType, Byte weight) {
        BigDecimal score = BigDecimal.valueOf(weight);

        if (preferenceType == PreferenceType.DISLIKE) {
            return score.negate();
        }

        if (preferenceType == PreferenceType.NEUTRAL) {
            return BigDecimal.ZERO;
        }

        return score;
    }

    private void saveOrUpdatePreferenceTag(
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