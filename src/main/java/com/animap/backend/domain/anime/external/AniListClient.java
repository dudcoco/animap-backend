package com.animap.backend.domain.anime.external;

import com.animap.backend.domain.anime.dto.ExternalAnimeSearchResponse;
import com.animap.backend.domain.anime.dto.ExternalMediaTagResponse;
import com.animap.backend.domain.anime.dto.ExternalTagListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AniListClient {

    private static final String ANILIST_API_URL = "https://graphql.anilist.co";

    private final RestClient.Builder restClientBuilder;
    private final ObjectMapper objectMapper;

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
                .contentType(MediaType.APPLICATION_JSON)
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

    public ExternalAnimeSearchResponse getAnimeById(Integer anilistId) {
        RestClient restClient = restClientBuilder.build();

        AniListGraphQlRequest request = new AniListGraphQlRequest(
                getAnimeByIdQuery(),
                Map.of("id", anilistId)
        );

        AniListGraphQlResponse response = restClient.post()
                .uri(ANILIST_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(AniListGraphQlResponse.class);

        if (response == null
                || response.data() == null
                || response.data().Media() == null) {
            throw new IllegalArgumentException("AniList에서 애니메이션을 찾을 수 없습니다.");
        }

        AniListGraphQlResponse.Media media = response.data().Media();

        return new ExternalAnimeSearchResponse(
                media.id(),
                media.title().nativeTitle(),
                media.title().english(),
                media.title().romaji(),
                media.description(),
                media.coverImage() != null ? media.coverImage().large() : null,
                media.seasonYear() != null ? media.seasonYear().shortValue() : null,
                media.averageScore(),
                media.status()
        );
    }

    public ExternalTagListResponse getExternalTags() {
        RestClient restClient = restClientBuilder.build();

        AniListGraphQlRequest request = new AniListGraphQlRequest(
                getExternalTagsQuery(),
                Map.of()
        );

        String responseBody = restClient.post()
                .uri(ANILIST_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(String.class);

        return parseExternalTags(responseBody);
    }

    private ExternalTagListResponse parseExternalTags(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode data = root.get("data");

            if (data == null || data.isNull()) {
                throw new IllegalArgumentException("AniList 응답에서 data를 찾을 수 없습니다.");
            }

            List<String> genres = parseGenres(data);
            List<ExternalMediaTagResponse> tags = parseMediaTags(data);

            return new ExternalTagListResponse(genres, tags);

        } catch (Exception e) {
            throw new IllegalArgumentException("AniList 태그 목록을 해석하는 중 오류가 발생했습니다.");
        }
    }

    private List<String> parseGenres(JsonNode data) {
        List<String> genres = new ArrayList<>();

        JsonNode genreCollection = data.get("GenreCollection");

        if (genreCollection == null || !genreCollection.isArray()) {
            return genres;
        }

        for (JsonNode genreNode : genreCollection) {
            genres.add(genreNode.asText());
        }

        return genres;
    }

    private List<ExternalMediaTagResponse> parseMediaTags(JsonNode data) {
        List<ExternalMediaTagResponse> tags = new ArrayList<>();

        JsonNode mediaTagCollection = data.get("MediaTagCollection");

        if (mediaTagCollection == null || !mediaTagCollection.isArray()) {
            return tags;
        }

        for (JsonNode tagNode : mediaTagCollection) {
            tags.add(new ExternalMediaTagResponse(
                    getIntegerValue(tagNode, "id"),
                    getTextValue(tagNode, "name"),
                    getTextValue(tagNode, "category"),
                    getTextValue(tagNode, "description"),
                    getBooleanValue(tagNode, "isAdult"),
                    getBooleanValue(tagNode, "isGeneralSpoiler"),
                    getBooleanValue(tagNode, "isMediaSpoiler")
            ));
        }

        return tags;
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

    private String getAnimeByIdQuery() {
        return """
                query ($id: Int) {
                  Media(id: $id, type: ANIME) {
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
                """;
    }

    private String getExternalTagsQuery() {
        return """
                query {
                  GenreCollection
                  MediaTagCollection {
                    id
                    name
                    description
                    category
                    isAdult
                    isGeneralSpoiler
                    isMediaSpoiler
                  }
                }
                """;
    }

    private String getTextValue(JsonNode node, String fieldName) {
        JsonNode valueNode = node.get(fieldName);

        if (valueNode == null || valueNode.isNull()) {
            return null;
        }

        return valueNode.asText();
    }

    private Integer getIntegerValue(JsonNode node, String fieldName) {
        JsonNode valueNode = node.get(fieldName);

        if (valueNode == null || valueNode.isNull()) {
            return null;
        }

        return valueNode.asInt();
    }

    private Boolean getBooleanValue(JsonNode node, String fieldName) {
        JsonNode valueNode = node.get(fieldName);

        if (valueNode == null || valueNode.isNull()) {
            return null;
        }

        return valueNode.asBoolean();
    }
}