package com.animap.backend.domain.preference;

import com.animap.backend.domain.tag.Tag;
import com.animap.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "user_preference_tags",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_preference_tag",
                        columnNames = {"user_id", "tag_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPreferenceTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Column(name = "preference_score", nullable = false, precision = 4, scale = 2)
    private BigDecimal preferenceScore;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static UserPreferenceTag create(
            User user,
            Tag tag,
            BigDecimal preferenceScore
    ) {
        UserPreferenceTag userPreferenceTag = new UserPreferenceTag();
        userPreferenceTag.user = user;
        userPreferenceTag.tag = tag;
        userPreferenceTag.preferenceScore = preferenceScore;
        return userPreferenceTag;
    }

    public void updateScore(BigDecimal preferenceScore) {
        this.preferenceScore = preferenceScore;
    }

    public void addScore(BigDecimal scoreToAdd) {
        this.preferenceScore = this.preferenceScore.add(scoreToAdd);
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