package com.animap.backend.domain.preference;

import com.animap.backend.domain.analysis.AnalysisSentiment;
import com.animap.backend.domain.analysis.RecordAnalysisTag;
import com.animap.backend.domain.analysis.RecordAnalysisTagRepository;
import com.animap.backend.domain.preference.dto.PreferenceAnalyzeResponse;
import com.animap.backend.domain.preference.dto.PreferenceTagResponse;
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
    private final RecordAnalysisTagRepository recordAnalysisTagRepository;

    @Transactional
    public PreferenceAnalyzeResponse analyzeMyPreferences(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        userPreferenceTagRepository.deleteAllByUserId(userId);

        List<UserAnimeRecord> records = recordRepository.findAllByUserId(userId);

        int updatedPreferenceTagCount = 0;

        updatedPreferenceTagCount += reflectAnimeTags(user, records);
        updatedPreferenceTagCount += reflectRecordAnalysisTags(user);

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

    private int reflectAnimeTags(
            User user,
            List<UserAnimeRecord> records
    ) {
        int reflectedCount = 0;

        for (UserAnimeRecord record : records) {
            List<AnimeTag> animeTags = animeTagRepository.findAllByAnime_Id(
                    record.getAnime().getId()
            );

            for (AnimeTag animeTag : animeTags) {
                BigDecimal score = calculateAnimeTagScore(
                        record.getPreferenceType(),
                        animeTag.getWeight()
                );

                if (score.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                saveOrUpdatePreferenceTag(
                        user,
                        animeTag.getTag(),
                        score
                );

                reflectedCount++;
            }
        }

        return reflectedCount;
    }

    private int reflectRecordAnalysisTags(User user) {
        List<RecordAnalysisTag> analysisTags =
                recordAnalysisTagRepository.findAllByRecord_User_Id(user.getId());

        int reflectedCount = 0;

        for (RecordAnalysisTag analysisTag : analysisTags) {
            BigDecimal score = calculateAnalysisTagScore(analysisTag);

            saveOrUpdatePreferenceTag(
                    user,
                    analysisTag.getTag(),
                    score
            );

            reflectedCount++;
        }

        return reflectedCount;
    }

    private BigDecimal calculateAnimeTagScore(
            PreferenceType preferenceType,
            Byte weight
    ) {
        BigDecimal score = BigDecimal.valueOf(weight);

        if (preferenceType == PreferenceType.LIKE) {
            return score;
        }

        if (preferenceType == PreferenceType.DISLIKE) {
            return score.negate();
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal calculateAnalysisTagScore(RecordAnalysisTag analysisTag) {
        BigDecimal score = BigDecimal.valueOf(analysisTag.getConfidence());

        if (analysisTag.getSentiment() == AnalysisSentiment.NEGATIVE) {
            return score.negate();
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