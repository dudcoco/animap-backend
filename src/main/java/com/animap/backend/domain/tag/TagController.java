package com.animap.backend.domain.tag;

import com.animap.backend.domain.tag.dto.AnimeTagCreateRequest;
import com.animap.backend.domain.tag.dto.AnimeTagCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anime")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping("/{animeId}/tags")
    public ResponseEntity<AnimeTagCreateResponse> addTagToAnime(
            @PathVariable Long animeId,
            @Valid @RequestBody AnimeTagCreateRequest request
    ) {
        AnimeTagCreateResponse response = tagService.addTagToAnime(animeId, request);

        return ResponseEntity.ok(response);
    }
}