package com.animap.backend.domain.preference;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPreferenceTagRepository extends JpaRepository<UserPreferenceTag, Long> {

    Optional<UserPreferenceTag> findByUserIdAndTagId(Long userId, Long tagId);

    boolean existsByUserIdAndTagId(Long userId, Long tagId);

    List<UserPreferenceTag> findAllByUserId(Long userId);

    // 분석할 때마다 기존 점수에 계속 더해지는 로직을 막음 > 분석 전에 이전 분석결과를 지우고 다시 계산
    void deleteAllByUserId(Long userId);
}