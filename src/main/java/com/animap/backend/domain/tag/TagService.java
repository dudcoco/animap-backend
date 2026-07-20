// 태그 등록 흐름
// 1. animeId로 애니가 실제 존재하는지 확인
// 2. 같은 name + category 태그가 이미 있는지 확인
// 3. 없으면 tags 테이블에 새 태그 생성
// 4. anime_tag에 애니-태그 연결
// 5. 이미 연결된 태그면 중복 저장하지 않음
package com.animap.backend.domain.tag;

import com.animap.backend.domain.anime.Anime;
import com.animap.backend.domain.anime.AnimeRepository;
import com.animap.backend.domain.tag.dto.AnimeTagCreateRequest;
import com.animap.backend.domain.tag.dto.AnimeTagCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final AnimeRepository animeRepository;
    private final TagRepository tagRepository;
    private final AnimeTagRepository animeTagRepository;

    @Transactional
    public AnimeTagCreateResponse addTagToAnime(Long animeId, AnimeTagCreateRequest request) {
        Anime anime = animeRepository.findById(animeId)
                .orElseThrow(() -> new IllegalArgumentException("애니메이션을 찾을 수 없습니다."));

        Tag tag = tagRepository.findByNameAndCategory(request.name(), request.category())
                .orElseGet(() -> tagRepository.save(Tag.create(request.name(), request.category())));

        if (animeTagRepository.existsByAnime_IdAndTag_Id(anime.getId(), tag.getId())) {
            return AnimeTagCreateResponse.alreadyExists(
                    anime.getId(),
                    tag.getId(),
                    tag.getName(),
                    tag.getCategory().name()
            );
        }

        Integer requestWeight = request.weight() != null ? request.weight() : 3;
        Byte weight = requestWeight.byteValue();

        AnimeTag animeTag = AnimeTag.create(anime, tag, weight);
        animeTagRepository.save(animeTag);

        return AnimeTagCreateResponse.of(
                anime.getId(),
                tag.getId(),
                tag.getName(),
                tag.getCategory().name()
        );
    }
}