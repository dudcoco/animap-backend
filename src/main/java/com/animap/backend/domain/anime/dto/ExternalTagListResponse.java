package com.animap.backend.domain.anime.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AniList 외부 장르 및 태그 목록 조회 응답")
public record ExternalTagListResponse(

        @Schema(
                description = "AniList에서 제공하는 장르 이름 목록",
                example = "[\"Action\", \"Adventure\", \"Comedy\"]"
        )
        List<String> genres,

        @Schema(
                description = "AniList에서 제공하는 상세 태그 목록"
        )
        List<ExternalMediaTagResponse> tags
) {
}
