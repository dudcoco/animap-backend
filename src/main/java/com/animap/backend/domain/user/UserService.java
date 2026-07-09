// 회원가입 요청이 들어오면
// 1. 이메일 중복확인
// 2. 닉네임 중복확인
// 3. User 객체생성
// 4. users 테이블에 저장
// 5. 저장된 user id 반환

package com.animap.backend.domain.user;

import com.animap.backend.domain.user.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long signUp(SignUpRequest request) {
        validateDuplicateEmail(request.email());
        validateDuplicateNickname(request.nickname());

        User user = User.createLocalUser(
                request.email(),
                request.password(),
                request.nickname()
        );

        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }

    private void validateDuplicateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
    }
}