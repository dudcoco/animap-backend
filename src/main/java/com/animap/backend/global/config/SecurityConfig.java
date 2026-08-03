package com.animap.backend.global.config;

import com.animap.backend.global.security.JwtAuthenticationEntryPoint;
import com.animap.backend.global.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Value("${app.cors.allowed-origins:http://localhost:5173,http://127.0.0.1:5173}")
    private String allowedOrigins;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
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

                        // Spring 내부 오류 처리 요청 허용
                        .dispatcherTypeMatchers(
                                DispatcherType.ERROR,
                                DispatcherType.FORWARD
                        ).permitAll()

                        // Swagger
                        .requestMatchers(
                                "/error",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // 회원가입 및 로그인
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users/signup",
                                "/api/users/login"
                        ).permitAll()

                        // 비로그인 허용: 애니메이션 조회
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/anime/search",
                                "/api/anime/search/external",
                                "/api/anime/external/tags",
                                "/api/anime/*"
                        ).permitAll()

                        // 비로그인 허용: 결과를 저장하지 않는 AI 분석
                        .requestMatchers(
                                HttpMethod.POST,
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

                        // 로그인 필요: 애니메이션 DB 변경
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/anime/import",
                                "/api/anime/*/tags"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/anime/*"
                        ).authenticated()

                        // 공개 경로로 명시하지 않은 API는 기본적으로 로그인 필요
                        .anyRequest().authenticated()
                )
                // 기본 로그인 필터보다 먼저 JWT 필터를 실행
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.stream(allowedOrigins.split(","))
                        .map(String::trim)
                        .filter(origin -> !origin.isBlank())
                        .toList()
        );
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
