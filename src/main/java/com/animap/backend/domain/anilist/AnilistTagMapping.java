package com.animap.backend.domain.anilist;

import com.animap.backend.domain.tag.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "anilist_tag_mappings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_anilist_tag_mapping_name",
                        columnNames = "anilist_tag_name"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnilistTagMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "anilist_tag_name", nullable = false, unique = true, length = 100)
    private String anilistTagName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static AnilistTagMapping create(String anilistTagName, Tag tag) {
        AnilistTagMapping mapping = new AnilistTagMapping();
        mapping.anilistTagName = anilistTagName;
        mapping.tag = tag;
        return mapping;
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