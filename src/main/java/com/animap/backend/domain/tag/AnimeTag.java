package com.animap.backend.domain.tag;

import com.animap.backend.domain.anime.Anime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public static AnimeTag create(Anime anime, Tag tag) {
        AnimeTag animeTag = new AnimeTag();
        animeTag.id = new AnimeTagId(anime.getId(), tag.getId());
        animeTag.anime = anime;
        animeTag.tag = tag;
        return animeTag;
    }
}