package com.animap.backend.global.config;

import com.animap.backend.global.security.JwtAuthenticationEntryPoint;
import com.animap.backend.global.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api/users/signup",
                                "/api/users/login"
                        ).permitAll()

                        // 비로그인 허용: 조회성 API만
                        .requestMatchers(HttpMethod.GET,
                                "/api/anime/search",
                                "/api/anime/*",
                                "/api/anime/search/external",
                                "/api/anime/external/tags"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST,
                                "/api/ai/review-analysis"
                        ).permitAll()

                        // 로그인 필요: 사용자 개인 데이터
                        .requestMatchers(
                                "/api/users/me",
                                "/api/records/**",
                                "/api/preferences/**",
                                "/api/recommendations/**",
                                "/api/ai/review-analysis/apply"
                        ).authenticated()

                        // 로그인 필요: DB 변경 애니 관리 API
                        .requestMatchers(HttpMethod.POST,
                                "/api/anime/import",
                                "/api/anime/*/tags"
                        ).authenticated()

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/anime/*"
                        ).authenticated()

                        .anyRequest().authenticated()
                )
                // 기본 로그인 필터보다 먼저 JWT 필터를 실행
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}