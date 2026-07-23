package com.animap.backend.domain.tag;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    // 태그 저장 시 이미 같은 태그가 있으면 새로 만들면 안 됨
    // 이미 있으면 기존 태그 사용, 태그가 없으면 새로 생성
    Optional<Tag> findByNameAndCategory(String name, TagType category);

    boolean existsByNameAndCategory(String name, TagType category);
}