package com.animap.backend.domain.record;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

public interface UserAnimeRecordRepository extends JpaRepository<UserAnimeRecord, Long> {

    // 감상 기록 중복 체크
    boolean existsByUserIdAndAnimeId(Long userId, Long animeId);

    // 내 감상 기록 목록 조회
    List<UserAnimeRecord> findAllByUserId(Long userId);

    Optional<UserAnimeRecord> findByIdAndUserId(Long recordId, Long userId);
}