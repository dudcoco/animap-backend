package com.animap.backend.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeTagRepository extends JpaRepository<AnimeTag, AnimeTagId> {

    // 애니에 태그를 붙일 때 중복 연결을 막아야 함 -> 이미 연결됐는지 확인할 수 있게 함
    boolean existsByAnime_IdAndTag_Id(Long animeId, Long tagId);

    // 상세 조회에서 작품 태그를 보여줄 때 사용
    List<AnimeTag> findAllByAnime_Id(Long animeId);

    // 사용자 선호 태그에 맞는 애니를 찾을 때 사용
    List<AnimeTag> findAllByTag_Id(Long tagId);
}