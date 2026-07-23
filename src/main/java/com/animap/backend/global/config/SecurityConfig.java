package com.animap.backend.global.config;

import com.animap.backend.global.security.JwtAuthenticationEntryPoint;
import com.animap.backend.global.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        // STATELESS: JWT방식은 서버가 로그인 세션을 저장하지 않고 클라이언트가 토큰을 들고 다님
                        // 세션을 만들지 않도록 STATELESS로 설정
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( // 애니 검색, 상세 조회는 로그인 안 해도 볼 수 있어야 함
                                "/api/users/signup",
                                "/api/users/login",
                                "/api/anime/search",
                                "/api/anime/**",
                                "/api/ai/**",
                                "/api/ai/review-analysis"
                        ).permitAll()
                        .requestMatchers(
                                "/api/users/me",
                                "/api/records/**",
                                "/api/preferences/**",
                                "/api/recommendations/**",
                                "/api/ai/review-analysis/apply"
                        ).authenticated() // 감상 기록은 반드시 로그인한 사용자에게 속해야 함
                        .anyRequest().permitAll()
                )
                // 기본 로그인 필터보다 먼저 JWT 필터를 실행
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}