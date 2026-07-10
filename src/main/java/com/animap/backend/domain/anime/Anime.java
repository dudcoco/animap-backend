package com.animap.backend.domain.anime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "anime")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "anilist_id", nullable = false, unique = true)
    private Integer anilistId;

    @Column(name = "title_ko", length = 255)
    private String titleKo;

    @Column(name = "title_original", nullable = false, length = 255)
    private String titleOriginal;

    @Column(name = "synopsis", columnDefinition = "TEXT")
    private String synopsis;

    @Column(name = "poster_url", length = 500)
    private String posterUrl;

    @Column(name = "release_year")
    private Short releaseYear;

    @Column(name = "average_score", precision = 5, scale = 2)
    private BigDecimal averageScore;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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