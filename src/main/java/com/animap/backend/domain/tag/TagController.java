package com.animap.backend.domain.tag;

import com.animap.backend.domain.tag.dto.AnimeTagCreateRequest;
import com.animap.backend.domain.tag.dto.AnimeTagCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "애니메이션 태그",
        description = "애니메이션에 표준 태그를 연결하는 API"
)
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/anime")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;


    @Operation(
            summary = "애니메이션에 태그 추가",
            description = "특정 애니메이션에 기존 표준 태그를 연결한다."
    )
    @PostMapping("/{animeId}/tags")
    public ResponseEntity<AnimeTagCreateResponse> addTagToAnime(

            @Parameter(
                    description = "태그를 추가할 애니메이션의 식별자",
                    example = "1"
            )
            @PathVariable Long animeId,
            @Valid @RequestBody AnimeTagCreateRequest request
    ) {
        AnimeTagCreateResponse response = tagService.addTagToAnime(animeId, request);

        return ResponseEntity.ok(response);
    }
}