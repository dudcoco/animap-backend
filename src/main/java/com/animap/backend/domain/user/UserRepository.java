// UserRepository: users 테이블에 접근하는 객체

package com.animap.backend.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email); // 이메일로 사용자 한 명 조회

    boolean existsByEmail(String email); // 이미 가입된 이메일인지 확인

    boolean existsByNickname(String nickname); // 이미 사용중인 닉네임인지 확인
}