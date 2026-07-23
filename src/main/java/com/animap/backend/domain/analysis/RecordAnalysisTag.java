package com.animap.backend.domain.analysis;

import com.animap.backend.domain.record.UserAnimeRecord;
import com.animap.backend.domain.tag.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "record_analysis_tags",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_record_analysis_tag",
                        columnNames = {"record_id", "tag_id", "sentiment"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordAnalysisTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // AI 분석 결과는 사용자 전체에 바로 붙는 게 아니라 특정 감상 기록에 붙어야 함
    // 같은 사용자가 여러 감상문을 쓸 수 있고
    // 각 감상문마다 좋아하거나 싫어한 요소가 다를 수 있기 때문
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private UserAnimeRecord record;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Enumerated(EnumType.STRING)
    @Column(name = "sentiment", nullable = false, length = 20)
    private AnalysisSentiment sentiment;

    @Column(name = "confidence", nullable = false)
    private Byte confidence;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static RecordAnalysisTag create(
            UserAnimeRecord record,
            Tag tag,
            AnalysisSentiment sentiment,
            Byte confidence
    ) {
        RecordAnalysisTag recordAnalysisTag = new RecordAnalysisTag();
        recordAnalysisTag.record = record;
        recordAnalysisTag.tag = tag;
        recordAnalysisTag.sentiment = sentiment;
        recordAnalysisTag.confidence = confidence;
        return recordAnalysisTag;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}