package com.animap.backend.domain.anime.external;

import java.util.Map;

public record AniListGraphQlRequest(
        String query,
        Map<String, Object> variables
) {
}