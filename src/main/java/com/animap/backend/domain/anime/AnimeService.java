package com.animap.backend.domain.anime;

import com.animap.backend.domain.anime.dto.AnimeDetailResponse;
import com.animap.backend.domain.anime.dto.AnimeSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.animap.backend.domain.anime.dto.ExternalAnimeSearchResponse;
import com.animap.backend.domain.anime.external.AniListClient;
import com.animap.backend.domain.anime.dto.AnimeImportRequest;
import com.animap.backend.domain.anime.dto.AnimeImportResponse;
import java.math.BigDecimal;
import com.animap.backend.domain.tag.AnimeTagRepository;
import com.animap.backend.domain.anime.dto.AnimeTagResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimeService {

    private final AnimeRepository animeRepository;
    private final AniListClient aniListClient;
    private final AnimeTagRepository animeTagRepository;

    public List<AnimeSearchResponse> searchAnime(String keyword) {
        return animeRepository
                .findByTitleKoContainingOrTitleOriginalContaining(keyword, keyword)
                .stream()
                .map(AnimeSearchResponse::from)
                .toList();
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
        return aniListClient.searchAnime(keyword);
    }

    @Transactional
    public AnimeImportResponse importAnime(AnimeImportRequest request) {
        return animeRepository.findByAnilistId(request.anilistId())
                .map(AnimeImportResponse::alreadyExists)
                .orElseGet(() -> saveAnimeFromAniList(request.anilistId()));
    }

    private AnimeImportResponse saveAnimeFromAniList(Integer anilistId) {
        ExternalAnimeSearchResponse externalAnime = aniListClient.getAnimeById(anilistId);

        BigDecimal averageScore = externalAnime.averageScore() != null
                ? BigDecimal.valueOf(externalAnime.averageScore())
                : null;

        String titleOriginal = externalAnime.titleOriginal() != null
                ? externalAnime.titleOriginal()
                : externalAnime.titleRomaji();

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

        return AnimeImportResponse.saved(savedAnime);
    }
}