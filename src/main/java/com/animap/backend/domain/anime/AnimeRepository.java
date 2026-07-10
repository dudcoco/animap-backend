package com.animap.backend.domain.anime;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

    Optional<Anime> findByAnilistId(Integer anilistId);

    boolean existsByAnilistId(Integer anilistId);

    List<Anime> findByTitleKoContainingOrTitleOriginalContaining(
            String titleKo,
            String titleOriginal
    );
}