package com.animap.backend.domain.analysis;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordAnalysisTagRepository extends JpaRepository<RecordAnalysisTag, Long> {

    // 해당 감상 기록이 이미 AI 분석된 적이 있는지 확인
    boolean existsByRecord_Id(Long recordId);

    List<RecordAnalysisTag> findAllByRecord_Id(Long recordId);

    void deleteAllByRecord_Id(Long recordId);

    // RecordAnalysisTag의 record의 user의 id가 userId인 것들을 전부 찾아라
    List<RecordAnalysisTag> findAllByRecord_User_Id(Long userId);
}
