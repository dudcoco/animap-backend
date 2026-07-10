package com.animap.backend.domain.anime;

import com.animap.backend.domain.anime.dto.AnimeDetailResponse;
import com.animap.backend.domain.anime.dto.AnimeSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.animap.backend.domain.anime.dto.ExternalAnimeSearchResponse;
import com.animap.backend.domain.anime.external.AniListClient;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimeService {

    private final AnimeRepository animeRepository;
    private final AniListClient aniListClient;

    public List<AnimeSearchResponse> searchAnime(String keyword) {
        return animeRepository
                .findByTitleKoContainingOrTitleOriginalContaining(keyword, keyword)
                .stream()
                .map(AnimeSearchResponse::from)
                .toList();
    }

    public AnimeDetailResponse getAnimeDetail(Long animeId) {
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new IllegalArgumentException("애니메이션을 찾을 수 없습니다."));

        return AnimeDetailResponse.from(anime);
    }

    public List<ExternalAnimeSearchResponse> searchExternalAnime(String keyword) {
        return aniListClient.searchAnime(keyword);
    }
}