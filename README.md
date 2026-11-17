# AniMap Backend 🎬

사용자의 애니메이션 감상 기록과 감상평을 분석해 취향을 도출하고,
작품 태그를 기반으로 개인화된 애니메이션을 추천하는 백엔드 서비스입니다.

## 💡 주요 기능

- **회원 관리**
  - 회원가입 및 로그인
  - JWT 기반 사용자 인증
  - 로그인 사용자 정보 조회

- **애니메이션 검색 및 수집**
  - AniList GraphQL API 기반 작품 검색
  - 선택한 작품을 AniMap DB에 저장
  - 인기 애니메이션 일괄 등록
  - 작품별 상세 정보 및 태그 관리

- **감상 기록 관리**
  - 감상 작품, 선호 여부, 감상평 기록
  - 감상 기록 조회, 등록, 수정, 삭제

- **AI 감상평 분석**
  - OpenAI API를 활용한 선호·비선호 태그 추출
  - 분석 결과를 감상 기록별로 저장
  - 재분석 시 기존 결과를 새 결과로 교체해 중복 반영 방지

- **취향 분석 및 추천**
  - 전체 감상 기록과 AI 분석 결과를 기반으로 취향 점수 재계산
  - 사용자 취향 태그와 작품 태그의 가중치를 비교해 추천 순위 산정
  - 추천 태그와 주의 태그를 포함한 추천 이유 제공

## 🔄 서비스 흐름

```text
애니메이션 검색 → 작품 추가 → 감상 기록 작성
        → AI 감상평 분석 → 사용자 취향 재계산 → 개인화 애니메이션 추천
```

## 🛠️ 기술 스택

| 구분 | 기술 |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 4.1.0 |
| Database | MySQL |
| ORM | Spring Data JPA |
| Security | Spring Security, JWT |
| External API | AniList GraphQL API, OpenAI API |
| API Docs | Springdoc OpenAPI, Swagger UI |
| Build | Gradle |

## 🗂️ 프로젝트 구조

```text
src/main/java/com/animap/backend
├── domain
│   ├── user              # 회원가입, 로그인, 사용자 정보
│   ├── anime             # 애니메이션 검색, 저장, 상세 조회
│   ├── anilist           # AniList API 연동
│   ├── record            # 감상 기록 관리
│   ├── analysis          # 기록별 AI 분석 결과 관리
│   ├── preference        # 사용자 취향 점수 계산
│   ├── recommendation    # 개인화 추천
│   ├── tag               # 작품 및 취향 태그 관리
│   └── ai                # OpenAI API 연동
└── global                    # 보안, 예외 처리, 공통 설정
```

## 🔗 주요 API

| 기능 | Method | Endpoint |
| --- | --- | --- |
| 회원가입 | `POST` | `/api/users/signup` |
| 로그인 | `POST` | `/api/users/login` |
| 사용자 정보 조회 | `GET` | `/api/users/me` |
| 애니메이션 검색 | `GET` | `/api/anime/search` |
| 애니메이션 저장 | `POST` | `/api/anime/import` |
| 인기 작품 일괄 등록 | `POST` | `/api/anime/import/popular` |
| 감상 기록 등록 | `POST` | `/api/records` |
| 감상 기록 목록 조회 | `GET` | `/api/records` |
| 감상 기록 수정 | `PATCH` | `/api/records/{recordId}` |
| 감상 기록 삭제 | `DELETE` | `/api/records/{recordId}` |
| 기록별 AI 분석 | `POST` | `/api/records/{recordId}/ai-analysis` |
| 사용자 취향 재계산 | `POST` | `/api/preferences/analyze` |
| 사용자 취향 조회 | `GET` | `/api/preferences` |
| 애니메이션 추천 | `GET` | `/api/recommendations` |
