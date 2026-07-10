// 클라이언트가 선택한 AniList 작품 ID를 서버에 보내야 함
package com.animap.backend.domain.anime.dto;

import jakarta.validation.constraints.NotNull;

public record AnimeImportRequest(

        @NotNull(message = "AniList ID는 필수입니다.")
        Integer anilistId
) {
}