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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "애니메이션",
        description = "애니메이션 검색, 외부 데이터 조회, 가져오기 및 상세 조회 API"
)
@RestController
@RequestMapping("/api/anime")
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @Operation(
            summary = "저장된 애니메이션 검색",
            description = "AniMap 데이터베이스에 저장된 애니메이션을 제목 키워드로 검색한다."
    )
    @GetMapping("/search")
    public ResponseEntity<List<AnimeSearchResponse>> searchAnime(

            @Parameter(
                    description = "검색할 애니메이션 제목 키워드",
                    example = "나루토"
            )
            @RequestParam String keyword
    ) {
        List<AnimeSearchResponse> response =
                animeService.searchAnime(keyword);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "외부 애니메이션 검색",
            description = "AniList 외부 API에서 제목 키워드와 일치하는 애니메이션을 검색한다."
    )
    @GetMapping("/search/external")
    public ResponseEntity<List<ExternalAnimeSearchResponse>> searchExternalAnime(

            @Parameter(
                    description = "외부 API에서 검색할 애니메이션 제목 키워드",
                    example = "Naruto"
            )
            @RequestParam String keyword
    ) {
        List<ExternalAnimeSearchResponse> response =
                animeService.searchExternalAnime(keyword);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "외부 애니메이션 가져오기",
            description = """
                    외부 애니메이션 식별자를 사용하여 AniList에서 작품 정보를 조회하고
                    AniMap 데이터베이스에 애니메이션과 매핑 가능한 표준 태그를 저장한다.
                    """
    )
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/import")
    public ResponseEntity<AnimeImportResponse> importAnime(
            @Valid @RequestBody AnimeImportRequest request
    ) {
        AnimeImportResponse response = animeService.importAnime(request);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "외부 태그 목록 조회",
            description = "AniList에서 제공하는 장르와 태그 목록을 조회한다."
    )
    @GetMapping("/external/tags")
    public ResponseEntity<ExternalTagListResponse> getExternalTags() {
        ExternalTagListResponse response = animeService.getExternalTags();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "애니메이션 상세 조회",
            description = "AniMap 데이터베이스에 저장된 애니메이션의 상세 정보와 태그 목록을 조회한다."
    )
    @GetMapping("/{animeId}")
    public ResponseEntity<AnimeDetailResponse> getAnimeDetail(

            @Parameter(
                    description = "조회할 애니메이션의 식별자",
                    example = "1"
            )
            @PathVariable Long animeId
    ) {
        AnimeDetailResponse response =
                animeService.getAnimeDetail(animeId);

        return ResponseEntity.ok(response);
    }
}