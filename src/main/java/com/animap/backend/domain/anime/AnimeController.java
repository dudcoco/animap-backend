package com.animap.backend.domain.anime;

import com.animap.backend.domain.anime.dto.AnimeDetailResponse;
import com.animap.backend.domain.anime.dto.AnimeSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.animap.backend.domain.anime.dto.ExternalAnimeSearchResponse;
import com.animap.backend.domain.anime.dto.AnimeImportRequest;
import com.animap.backend.domain.anime.dto.AnimeImportResponse;
import jakarta.validation.Valid;
import com.animap.backend.domain.anime.dto.ExternalTagListResponse;

@RestController
@RequestMapping("/api/anime")
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping("/search")
    public ResponseEntity<List<AnimeSearchResponse>> searchAnime(
            @RequestParam String keyword
    ) {
        List<AnimeSearchResponse> response = animeService.searchAnime(keyword);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/external")
    public ResponseEntity<List<ExternalAnimeSearchResponse>> searchExternalAnime(
            @RequestParam String keyword
    ) {
        List<ExternalAnimeSearchResponse> response = animeService.searchExternalAnime(keyword);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/import")
    public ResponseEntity<AnimeImportResponse> importAnime(
            @Valid @RequestBody AnimeImportRequest request
    ) {
        AnimeImportResponse response = animeService.importAnime(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/external/tags")
    public ResponseEntity<ExternalTagListResponse> getExternalTags() {
        ExternalTagListResponse response = animeService.getExternalTags();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{animeId}")
    public ResponseEntity<AnimeDetailResponse> getAnimeDetail(
            @PathVariable Long animeId
    ) {
        AnimeDetailResponse response = animeService.getAnimeDetail(animeId);

        return ResponseEntity.ok(response);
    }
}