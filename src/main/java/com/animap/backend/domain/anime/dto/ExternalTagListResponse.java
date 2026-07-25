package com.animap.backend.domain.anime.dto;

import java.util.List;

public record ExternalTagListResponse(
        List<String> genres,
        List<ExternalMediaTagResponse> tags
) {
}
