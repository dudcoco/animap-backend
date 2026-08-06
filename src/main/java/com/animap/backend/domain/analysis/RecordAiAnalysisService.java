package com.animap.backend.domain.analysis;

import com.animap.backend.domain.ai.OpenAiClient;
import com.animap.backend.domain.ai.dto.AnalyzedTagResponse;
import com.animap.backend.domain.ai.dto.ReviewAnalysisResponse;
import com.animap.backend.domain.analysis.dto.RecordAiAnalysisResponse;
import com.animap.backend.domain.record.UserAnimeRecord;
import com.animap.backend.domain.record.UserAnimeRecordRepository;
import com.animap.backend.domain.tag.Tag;
import com.animap.backend.domain.tag.TagRepository;
import com.animap.backend.domain.tag.TagType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordAiAnalysisService {

    private final OpenAiClient openAiClient;
    private final UserAnimeRecordRepository recordRepository;
    private final TagRepository tagRepository;
    private final RecordAnalysisTagRepository recordAnalysisTagRepository;

    @Transactional
    public RecordAiAnalysisResponse analyzeRecordAndApply(
            Long userId,
            Long recordId
    ) {
        // 1. 감상 기록 조회
        UserAnimeRecord record = recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new IllegalArgumentException("감상 기록을 찾을 수 없습니다."));

        // 2. ReviewText 확인 > 감상 이유가 비어있으면 AI가 분석할 문장이 없으니 불가
        validateReviewText(record);

        // 3. AI 분석 실행. 외부 호출이 실패하면 기존 분석 결과는 유지한다.
        List<Tag> availableTags = tagRepository.findAll();

        ReviewAnalysisResponse analysisResult =
                openAiClient.analyzeReview(record.getReviewText(), availableTags);

        // 4. 분석에 성공한 뒤 기존 결과를 지우고 새 결과로 교체한다.
        recordAnalysisTagRepository.deleteAllByRecord_Id(recordId);
        recordAnalysisTagRepository.flush();

        // 5. 긍정 태그/부정 태그를 기록별 분석 테이블에 저장한다.
        int reflectedTagCount = 0;

        for (AnalyzedTagResponse positiveTag : analysisResult.positiveTags()) {
            if (reflectAnalysisResult(
                    record,
                    positiveTag,
                    AnalysisSentiment.POSITIVE
            )) {
                reflectedTagCount++;
            }
        }

        for (AnalyzedTagResponse negativeTag : analysisResult.negativeTags()) {
            if (reflectAnalysisResult(
                    record,
                    negativeTag,
                    AnalysisSentiment.NEGATIVE
            )) {
                reflectedTagCount++;
            }
        }

        return RecordAiAnalysisResponse.of(
                record.getId(),
                analysisResult.positiveTags().size(),
                analysisResult.negativeTags().size(),
                reflectedTagCount,
                analysisResult.summary()
        );
    }

    private void validateReviewText(UserAnimeRecord record) {
        if (record.getReviewText() == null || record.getReviewText().isBlank()) {
            throw new IllegalArgumentException("AI 분석을 수행할 감상 이유가 없습니다.");
        }
    }

    private boolean reflectAnalysisResult(
            UserAnimeRecord record,
            AnalyzedTagResponse analyzedTag,
            AnalysisSentiment sentiment
    ) {
        TagType category = parseTagType(analyzedTag.category());

        // 6. AI가 목록에 없는 태그를 반환하면 DB에 새로 만들지 X, 그대로 반영하지 X
        Tag tag = tagRepository.findByName(analyzedTag.name())
                .orElse(null);

        if (tag == null) {
            return false;
        }

        Byte confidence = convertConfidence(analyzedTag.confidence());

        // 7. record_analysis_tags 저장
        // 이걸 저장해야 이 감상기록에서 어떤 태그가 AI 분석으로 나왔는지 남길 수 있음
        RecordAnalysisTag recordAnalysisTag = RecordAnalysisTag.create(
                record,
                tag,
                sentiment,
                confidence
        );

        recordAnalysisTagRepository.save(recordAnalysisTag);
        return true;
    }

    private TagType parseTagType(String category) {
        try {
            return TagType.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 태그 카테고리입니다: " + category);
        }
    }

    private Byte convertConfidence(int confidence) {
        if (confidence < 1 || confidence > 5) {
            throw new IllegalArgumentException("confidence는 1부터 5 사이여야 합니다.");
        }

        return (byte) confidence;
    }

}
