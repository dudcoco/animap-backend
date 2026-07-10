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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

    private final UserAnimeRecordRepository recordRepository;
    private final UserRepository userRepository;
    private final AnimeRepository animeRepository;

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

    private void validateDuplicateRecord(Long userId, Long animeId) {
        if (recordRepository.existsByUserIdAndAnimeId(userId, animeId)) {
            throw new IllegalArgumentException("이미 등록한 작품입니다.");
        }
    }
}