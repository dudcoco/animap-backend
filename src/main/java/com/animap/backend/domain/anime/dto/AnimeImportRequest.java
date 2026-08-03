// 클라이언트가 선택한 AniList 작품 ID를 서버에 보내야 함
package com.animap.backend.domain.anime.dto;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AniList 애니메이션 가져오기 요청")
public record AnimeImportRequest(

        @Schema(
                description = "AniList에서 사용하는 애니메이션 식별자",
                example = "20"
        )
        @NotNull(message = "AniList ID는 필수입니다.")
        Integer anilistId
) {
}