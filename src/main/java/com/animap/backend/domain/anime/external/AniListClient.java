package com.animap.backend.domain.anime.external;

import com.animap.backend.domain.anime.dto.ExternalAnimeSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AniListClient {

    private static final String ANILIST_API_URL = "https://graphql.anilist.co";

    private final RestClient.Builder restClientBuilder;

    public List<ExternalAnimeSearchResponse> searchAnime(String keyword) {
        RestClient restClient = restClientBuilder.build();

        AniListGraphQlRequest request = new AniListGraphQlRequest(
                getSearchQuery(),
                Map.of(
                        "search", keyword,
                        "page", 1,
                        "perPage", 10
                )
        );

        AniListGraphQlResponse response = restClient.post()
                .uri(ANILIST_API_URL)
                .body(request)
                .retrieve()
                .body(AniListGraphQlResponse.class);

        if (response == null || response.data() == null || response.data().Page() == null) {
            return List.of();
        }

        return response.data().Page().media()
                .stream()
                .map(media -> new ExternalAnimeSearchResponse(
                        media.id(),
                        media.title().nativeTitle(),
                        media.title().english(),
                        media.title().romaji(),
                        media.description(),
                        media.coverImage() != null ? media.coverImage().large() : null,
                        media.seasonYear() != null ? media.seasonYear().shortValue() : null,
                        media.averageScore(),
                        media.status()
                ))
                .toList();
    }

    private String getSearchQuery() {
        return """
                query ($search: String, $page: Int, $perPage: Int) {
                  Page(page: $page, perPage: $perPage) {
                    media(search: $search, type: ANIME) {
                      id
                      title {
                        romaji
                        english
                        nativeTitle: native
                      }
                      description(asHtml: false)
                      coverImage {
                        large
                      }
                      seasonYear
                      averageScore
                      status
                    }
                  }
                }
                """;
    }
}