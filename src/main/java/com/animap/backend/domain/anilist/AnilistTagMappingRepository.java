package com.animap.backend.domain.anilist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnilistTagMappingRepository extends JpaRepository<AnilistTagMapping, Long> {

    Optional<AnilistTagMapping> findByAnilistTagName(String anilistTagName);

    List<AnilistTagMapping> findAllByAnilistTagNameIn(List<String> anilistTagNames);
}
