package com.animap.backend.domain.record;

import com.animap.backend.domain.anime.Anime;
import com.animap.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "user_anime_records",
        // 같은 사용자가 같은 애니를 중복 등록하지 못하게 하는 장치
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_anime_record",
                        columnNames = {"user_id", "anime_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAnimeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 감상 기록은 반드시 특정 사용자에게 속함
    // 같은 애니라도 사용자마다 기록이 다를 수 있기 때문에 User와 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 감상 기록은 반드시 특정 애니에 대한 기록임
    // Anime와 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id", nullable = false)
    private Anime anime;

    // 평점
    @Column(name = "rating")
    private Integer rating;

    // 좋아했던 작품인지, 별로였던 작품인지 구분
    @Enumerated(EnumType.STRING)
    @Column(name = "preference_type", nullable = false, length = 20)
    private PreferenceType preferenceType;

    // 사용자가 적은 감상 이유
    @Column(name = "review_text", columnDefinition = "TEXT")
    private String reviewText;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static UserAnimeRecord create(
            User user,
            Anime anime,
            Integer rating,
            PreferenceType preferenceType,
            String reviewText
    ) {
        UserAnimeRecord record = new UserAnimeRecord();
        record.user = user;
        record.anime = anime;
        record.rating = rating;
        record.preferenceType = preferenceType;
        record.reviewText = reviewText;
        return record;
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