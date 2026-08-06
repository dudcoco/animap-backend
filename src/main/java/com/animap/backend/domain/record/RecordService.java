package com.animap.backend.domain.record;

import com.animap.backend.domain.anime.Anime;
import com.animap.backend.domain.anime.AnimeRepository;
import com.animap.backend.domain.record.dto.RecordCreateRequest;
import com.animap.backend.domain.record.dto.RecordCreateResponse;
import com.animap.backend.domain.user.User;
import com.animap.backend.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.animap.backend.domain.record.dto.RecordListResponse;
import java.util.List;
import com.animap.backend.domain.record.dto.RecordDetailResponse;
import com.animap.backend.domain.record.dto.RecordUpdateRequest;
import com.animap.backend.domain.record.dto.RecordUpdateResponse;
import com.animap.backend.domain.record.dto.RecordDeleteResponse;
import com.animap.backend.domain.anime.dto.AnimeTagResponse;
import com.animap.backend.domain.tag.AnimeTagRepository;
import com.animap.backend.domain.analysis.RecordAnalysisTagRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final UserAnimeRecordRepository recordRepository;
    private final UserRepository userRepository;
    private final AnimeRepository animeRepository;
    private final AnimeTagRepository animeTagRepository;
    private final RecordAnalysisTagRepository recordAnalysisTagRepository;

    // 생성
    @Transactional
    public RecordCreateResponse createRecord(Long userId, RecordCreateRequest request) {
        // 1. 요청한 사용자가 실제로 존재하는가?
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 요청한 애니가 실제로 존재하는가?
        Anime anime = animeRepository.findById(request.animeId())
                .orElseThrow(() -> new IllegalArgumentException("애니메이션을 찾을 수 없습니다."));

        // 3. 같은 사용자가 같은 애니를 이미 등록하지 않았는가?
        validateDuplicateRecord(userId, request.animeId());

        UserAnimeRecord record = UserAnimeRecord.create(
                user,
                anime,
                request.rating(),
                request.preferenceType(),
                request.reviewText()
        );

        UserAnimeRecord savedRecord = recordRepository.save(record);

        return RecordCreateResponse.of(savedRecord.getId());
    }

    @Transactional(readOnly = true)
    public List<RecordListResponse> getMyRecords(Long userId) {
        return recordRepository.findAllByUserId(userId)
                .stream()
                .map(record -> {
                    List<AnimeTagResponse> tags = getAnimeTags(record.getAnime().getId());
                    return RecordListResponse.of(record, tags);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public RecordDetailResponse getMyRecordDetail(Long userId, Long recordId) {
        UserAnimeRecord record = recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new IllegalArgumentException("감상 기록을 찾을 수 없습니다."));

        List<AnimeTagResponse> tags = getAnimeTags(record.getAnime().getId());

        return RecordDetailResponse.of(record, tags);
    }

    // 수정
    @Transactional
    public RecordUpdateResponse updateRecord(
            Long userId,
            Long recordId,
            RecordUpdateRequest request
    ) {
        UserAnimeRecord record = recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new IllegalArgumentException("감상 기록을 찾을 수 없습니다."));

        boolean reviewTextChanged = request.reviewText() != null
                && !Objects.equals(record.getReviewText(), request.reviewText());

        record.update(
                request.rating(),
                request.preferenceType(),
                request.reviewText()
        );

        if (reviewTextChanged) {
            recordAnalysisTagRepository.deleteAllByRecord_Id(recordId);
        }

        return RecordUpdateResponse.of(record.getId());
    }

    // 삭제
    @Transactional
    public RecordDeleteResponse deleteRecord(Long userId, Long recordId) {
        UserAnimeRecord record = recordRepository.findByIdAndUserId(recordId, userId)
                .orElseThrow(() -> new IllegalArgumentException("감상 기록을 찾을 수 없습니다."));

        recordAnalysisTagRepository.deleteAllByRecord_Id(recordId);
        recordAnalysisTagRepository.flush();
        recordRepository.delete(record);

        return RecordDeleteResponse.of(recordId);
    }

    private void validateDuplicateRecord(Long userId, Long animeId) {
        if (recordRepository.existsByUserIdAndAnimeId(userId, animeId)) {
            throw new IllegalArgumentException("이미 등록한 작품입니다.");
        }
    }

    private List<AnimeTagResponse> getAnimeTags(Long animeId) {
        return animeTagRepository.findAllByAnime_Id(animeId)
                .stream()
                .map(AnimeTagResponse::from)
                .toList();
    }
}
