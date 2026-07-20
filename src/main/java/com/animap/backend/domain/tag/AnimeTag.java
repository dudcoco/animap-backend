package com.animap.backend.domain.tag;

import com.animap.backend.domain.anime.Anime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "anime_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimeTag {

    @EmbeddedId
    private AnimeTagId id;

    @MapsId("animeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id", nullable = false)
    private Anime anime;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Column(name = "weight", nullable = false)
    private Byte weight;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static AnimeTag create(Anime anime, Tag tag, Byte weight) {
        AnimeTag animeTag = new AnimeTag();
        animeTag.id = new AnimeTagId(anime.getId(), tag.getId());
        animeTag.anime = anime;
        animeTag.tag = tag;
        animeTag.weight = weight;
        return animeTag;
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