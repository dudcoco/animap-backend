package com.animap.backend.domain.anime;

import com.animap.backend.domain.anime.dto.AnimeDetailResponse;
import com.animap.backend.domain.anime.dto.AnimeSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnimeService {

    private final AnimeRepository animeRepository;

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
}