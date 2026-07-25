package com.animap.backend.domain.anime.dto;

public record ExternalMediaTagResponse(
        Integer id,
        String name,
        String category,
        String description,
        Boolean isAdult,
        Boolean isGeneralSpoiler,
        Boolean isMediaSpoiler
) {
}