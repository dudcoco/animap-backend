package com.animap.backend.domain.anime;

import com.animap.backend.domain.anilist.AnilistTagMapping;
import com.animap.backend.domain.anilist.AnilistTagMappingRepository;
import com.animap.backend.domain.anime.dto.AnimeDetailResponse;
import com.animap.backend.domain.anime.dto.AnimeImportRequest;
import com.animap.backend.domain.anime.dto.AnimeImportResponse;
import com.animap.backend.domain.anime.dto.AnimeSearchResponse;
import com.animap.backend.domain.anime.dto.AnimeTagResponse;
import com.animap.backend.domain.anime.dto.ExternalAnimeSearchResponse;
import com.animap.backend.domain.anime.dto.ExternalTagListResponse;
import com.animap.backend.domain.anime.dto.AnimeBulkImportResponse;
import com.animap.backend.domain.anime.external.AniListClient;
import com.animap.backend.domain.tag.AnimeTag;
import com.animap.backend.domain.tag.AnimeTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimeService {

    private final AnimeRepository animeRepository;
    private final AniListClient aniListClient;
    private final AnimeTagRepository animeTagRepository;
    private final AnilistTagMappingRepository anilistTagMappingRepository;

    public List<ExternalAnimeSearchResponse> searchAnime(String keyword) {
        return searchExternalAnime(keyword);
    }

    @Transactional(readOnly = true)
    public AnimeDetailResponse getAnimeDetail(Long animeId) {
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new IllegalArgumentException("애니메이션을 찾을 수 없습니다."));

        List<AnimeTagResponse> tags = animeTagRepository.findAllByAnime_Id(animeId)
                .stream()
                .map(AnimeTagResponse::from)
                .toList();

        return AnimeDetailResponse.of(anime, tags);
    }

    public List<ExternalAnimeSearchResponse> searchExternalAnime(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        return aniListClient.searchAnime(keyword.trim());
    }

    @Transactional
    public AnimeImportResponse importAnime(AnimeImportRequest request) {
        return animeRepository.findByAnilistId(request.anilistId())
                .map(AnimeImportResponse::alreadyExists)
                .orElseGet(() -> saveAnimeFromAniList(request.anilistId()));
    }

    @Transactional(readOnly = true)
    public ExternalTagListResponse getExternalTags() {
        return aniListClient.getExternalTags();
    }

    @Transactional
    public AnimeBulkImportResponse importPopularAnime(
            int page,
            int perPage
    ) {
        int normalizedPage = normalizePage(page);
        int normalizedPerPage = normalizePerPage(perPage);

        List<ExternalAnimeSearchResponse> externalAnimeList =
                aniListClient.getPopularAnime(normalizedPage, normalizedPerPage);

        int savedCount = 0;
        int skippedCount = 0;

        for (ExternalAnimeSearchResponse externalAnime : externalAnimeList) {
            if (externalAnime.anilistId() == null
                    || animeRepository.existsByAnilistId(externalAnime.anilistId())) {
                skippedCount++;
                continue;
            }

            saveAnimeFromExternalAnime(externalAnime);
            savedCount++;
        }

        return AnimeBulkImportResponse.of(
                externalAnimeList.size(),
                savedCount,
                skippedCount
        );
    }

    private AnimeImportResponse saveAnimeFromAniList(Integer anilistId) {
        ExternalAnimeSearchResponse externalAnime = aniListClient.getAnimeById(anilistId);

        Anime savedAnime = saveAnimeFromExternalAnime(externalAnime);

        return AnimeImportResponse.saved(savedAnime);
    }

    private Anime saveAnimeFromExternalAnime(ExternalAnimeSearchResponse externalAnime) {
        BigDecimal averageScore = externalAnime.averageScore() != null
                ? BigDecimal.valueOf(externalAnime.averageScore())
                : null;

        String titleOriginal = externalAnime.titleRomaji() != null
                ? externalAnime.titleRomaji()
                : externalAnime.titleEnglish();

        Anime anime = Anime.createFromAniList(
                externalAnime.anilistId(),
                externalAnime.titleKo(),
                titleOriginal,
                externalAnime.synopsis(),
                externalAnime.posterUrl(),
                externalAnime.releaseYear(),
                averageScore,
                externalAnime.status()
        );

        Anime savedAnime = animeRepository.save(anime);

        saveMappedAnimeTags(savedAnime, externalAnime);

        return savedAnime;
    }

    private int normalizePage(int page) {
        if (page < 1) {
            return 1;
        }

        return page;
    }

    private int normalizePerPage(int perPage) {
        if (perPage < 1) {
            return 10;
        }

        return Math.min(perPage, 50);
    }

    private void saveMappedAnimeTags(
            Anime anime,
            ExternalAnimeSearchResponse externalAnime
    ) {
        List<String> anilistTagNames = collectAnilistTagNames(externalAnime);

        if (anilistTagNames.isEmpty()) {
            return;
        }

        List<AnilistTagMapping> mappings =
                anilistTagMappingRepository.findAllByAnilistTagNameIn(anilistTagNames);

        for (AnilistTagMapping mapping : mappings) {
            boolean alreadyExists = animeTagRepository.existsByAnime_IdAndTag_Id(
                    anime.getId(),
                    mapping.getTag().getId()
            );

            if (alreadyExists) {
                continue;
            }

            animeTagRepository.save(
                    AnimeTag.create(
                            anime,
                            mapping.getTag(),
                            calculateDefaultWeight()
                    )
            );
        }
    }

    private List<String> collectAnilistTagNames(ExternalAnimeSearchResponse externalAnime) {
        Set<String> tagNames = new LinkedHashSet<>();

        if (externalAnime.genres() != null) {
            tagNames.addAll(externalAnime.genres());
        }

        if (externalAnime.tagNames() != null) {
            tagNames.addAll(externalAnime.tagNames());
        }

        return new ArrayList<>(tagNames);
    }

    private Byte calculateDefaultWeight() {
        return (byte) 3;
    }
}