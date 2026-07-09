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
import org.springframework.security.crypto.password.PasswordEncoder;
import com.animap.backend.domain.user.dto.LoginRequest;
import com.animap.backend.domain.user.dto.LoginResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signUp(SignUpRequest request) {
        validateDuplicateEmail(request.email());
        validateDuplicateNickname(request.nickname());

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.createLocalUser(
                request.email(),
                encodedPassword,
                request.nickname()
        );

        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }
    /**
     * 이메일로 사용자 조회
     * → 없으면 에러
     * → 입력 비밀번호와 암호화된 DB 비밀번호 비교
     * → 맞으면 로그인 성공 응답
     */
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        // request.password(): 비밀번호 원문, user.getPassword(): DB에 저장된 암호화된 비밀번호
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return LoginResponse.of(user.getId(), user.getNickname());
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