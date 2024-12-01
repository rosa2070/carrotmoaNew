package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.Accommodation;
import carrotmoa.carrotmoa.entity.Post;
import carrotmoa.carrotmoa.model.request.SaveAccommodationRequest;
import carrotmoa.carrotmoa.model.request.UpdateAccommodationRequest;
import carrotmoa.carrotmoa.model.response.AccommodationSearchResponse;
import carrotmoa.carrotmoa.model.response.AccommodationSearchResponseImpl;
import carrotmoa.carrotmoa.model.response.CommunityPostSearchResponse;
import carrotmoa.carrotmoa.model.response.CommunityPostSearchResponseImpl;
import carrotmoa.carrotmoa.model.response.FleaMarketPostResponse;
import carrotmoa.carrotmoa.repository.AccommodationRepository;
import carrotmoa.carrotmoa.repository.FleaMarketPostRepository;
import carrotmoa.carrotmoa.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PostService {
    private static final Long SERVICE_ID = 8L;

    private final PostRepository postRepository;
    private final AccommodationRepository accommodationRepository;
    private final FleaMarketPostRepository fleaMarketPostRepository;

    public PostService(PostRepository postRepository, AccommodationRepository accommodationRepository, FleaMarketPostRepository fleaMarketPostRepository) {
        this.postRepository = postRepository;
        this.accommodationRepository = accommodationRepository;
        this.fleaMarketPostRepository = fleaMarketPostRepository;
    }

    public Post savePost(SaveAccommodationRequest saveAccommodationRequest) {
        Post post = Post.builder()
            .serviceId(SERVICE_ID)
            .userId(saveAccommodationRequest.getUserId())
            .title(saveAccommodationRequest.getTitle())
            .content(saveAccommodationRequest.getContent())
            .build();
        return postRepository.save(post);
    }

    public void updatePost(Long accommodationId, UpdateAccommodationRequest updateAccommodationRequest) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new IllegalArgumentException("숙소를 찾을 수 없습니다."));

        Post post = postRepository.findById(accommodation.getPostId())
            .orElseThrow(() -> new IllegalArgumentException("포스트를 찾을 수 없습니다."));

        log.info("방 이름 및 설명 변경 전: 제목: {}, 내용: {}", post.getTitle(), post.getContent());
        // update 메서드 호출
        post.updatePost(updateAccommodationRequest.getTitle(), updateAccommodationRequest.getContent());

        post.updatePost(updateAccommodationRequest.getTitle(),
            updateAccommodationRequest.getContent());

        log.info("방 이름 및 설명 변경 후: 제목: {}, 내용: {}", post.getTitle(), post.getContent());
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Slice<CommunityPostSearchResponse> integratedSearchCommunityPost(String keyword, int page, int size) {
        Long serviceId = 4L;
        Pageable pageable = PageRequest.of(page, size);

        // 네이티브 쿼리 호출 후 결과를 Slice 형태로 가져옴
        Slice<CommunityPostSearchResponse> searchResults = postRepository.integratedSearchCommunityPost(keyword, serviceId, pageable);

        // 필요시 변환하여 postUrl을 추가한 결과 반환
        List<CommunityPostSearchResponse> transformedResults = searchResults.getContent().stream()
            .map(result -> new CommunityPostSearchResponseImpl(
                result.getPostId(),
                result.getAddressName(),
                result.getContent(),
                result.getPostImageUrl()
            ))
            .collect(Collectors.toList());

        return new SliceImpl<>(transformedResults, pageable, searchResults.hasNext());
    }

    public void markAsDeleted(Long postId) {
        postRepository.markAsDeleted(postId);
    }

    @Transactional(readOnly = true)
    public Slice<FleaMarketPostResponse> integratedSearchFleaMarketPost(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return fleaMarketPostRepository.findByKeyword(keyword, pageable);
    }

    public Slice<AccommodationSearchResponse> integratedSearchAccommodationPost(String keyword, int page, int size) {
        Long serviceId = 8L;
        Pageable pageable = PageRequest.of(page, size);

        // 네이티브 쿼리 호출 후 결과를 Slice 형태로 가져옴
        Slice<AccommodationSearchResponse> searchResults = accommodationRepository.integratedSearchAccommodation(keyword, serviceId, pageable);

        List<AccommodationSearchResponse> transformedResults = searchResults.getContent().stream()
            .map(result -> new AccommodationSearchResponseImpl(
                result.getAccommodationId(),
                result.getTitle(),
                result.getRoadAddress(),
                result.getPrice(),
                result.getImageUrl(),
                result.getRoomCount(),
                result.getBathRoomCount(),
                result.getLivingRoomCount(),
                result.getKitchenCount()
            ))
            .collect(Collectors.toList());

        return new SliceImpl<>(transformedResults, pageable, searchResults.hasNext());
    }
}
