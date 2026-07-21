package com.animap.backend.domain.preference;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPreferenceTagRepository extends JpaRepository<UserPreferenceTag, Long> {

    Optional<UserPreferenceTag> findByUserIdAndTagId(Long userId, Long tagId);

    boolean existsByUserIdAndTagId(Long userId, Long tagId);

    List<UserPreferenceTag> findAllByUserId(Long userId);
}