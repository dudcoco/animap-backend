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

    // 외부 API에서 가져온 데이터를 Anime 객체로 만들어야 DB에 저장 가능
    public static Anime createFromAniList(
            Integer anilistId,
            String titleKo,
            String titleOriginal,
            String synopsis,
            String posterUrl,
            Short releaseYear,
            BigDecimal averageScore,
            String status
    ) {
        Anime anime = new Anime();
        anime.anilistId = anilistId;
        anime.titleKo = titleKo;
        anime.titleOriginal = titleOriginal;
        anime.synopsis = synopsis;
        anime.posterUrl = posterUrl;
        anime.releaseYear = releaseYear;
        anime.averageScore = averageScore;
        anime.status = status;
        return anime;
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