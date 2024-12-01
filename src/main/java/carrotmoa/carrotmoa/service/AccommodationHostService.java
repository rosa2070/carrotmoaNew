package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.Accommodation;
import carrotmoa.carrotmoa.entity.Post;
import carrotmoa.carrotmoa.model.request.SaveAccommodationRequest;
import carrotmoa.carrotmoa.model.request.UpdateAccommodationRequest;
import carrotmoa.carrotmoa.model.response.AccommodationDetailResponse;
import carrotmoa.carrotmoa.model.response.HostManagedAccommodationResponse;
import carrotmoa.carrotmoa.repository.AccommodationDetailCustomRepository;
import carrotmoa.carrotmoa.repository.AccommodationRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccommodationHostService {
    private final PostService postService;
    private final AccommodationService accommodationService;
    private final AccommodationSpaceService accommodationSpaceService;
    private final AccommodationAmenityService accommodationAmenityService;
    private final AccommodationImageService accommodationImageService;
    private final AccommodationDetailCustomRepository accommodationDetailCustomRepository;
    private final AccommodationRepository accommodationRepository;
    private final AccommodationLocationService accommodationLocationService;

    public AccommodationHostService(PostService postService,
        AccommodationService accommodationService,
        AccommodationSpaceService accommodationSpaceService,
        AccommodationAmenityService accommodationAmenityService,
        AccommodationImageService accommodationImageService,
        AccommodationDetailCustomRepository accommodationDetailCustomRepository,
        AccommodationRepository accommodationRepository,
        AccommodationLocationService accommodationLocationService) {
        this.postService = postService;
        this.accommodationService = accommodationService;
        this.accommodationSpaceService = accommodationSpaceService;
        this.accommodationAmenityService = accommodationAmenityService;
        this.accommodationImageService = accommodationImageService;
        this.accommodationDetailCustomRepository = accommodationDetailCustomRepository;
        this.accommodationRepository = accommodationRepository;
        this.accommodationLocationService = accommodationLocationService;
    }

    @Transactional
    public Long createAccommodation(SaveAccommodationRequest saveAccommodationRequest) {
        try {
            Post savedPost = postService.savePost(saveAccommodationRequest);
            Accommodation savedAccommodation = accommodationService.saveAccommodation(saveAccommodationRequest, savedPost.getId());
            accommodationSpaceService.saveAccommodationSpaces(savedAccommodation.getId(), saveAccommodationRequest);
            accommodationAmenityService.saveAccommodationAmenities(savedAccommodation.getId(), saveAccommodationRequest);
            accommodationImageService.saveAccommodationImages(savedAccommodation.getId(), saveAccommodationRequest.getImages());
            accommodationLocationService.saveAccommodationLocation(savedAccommodation.getId(), saveAccommodationRequest);
            return savedAccommodation.getId();
        } catch (IOException e) {
            log.error("Image upload failed: {}", e.getMessage());
            throw new RuntimeException("image upload failed", e);
        }

    }

    @Transactional
    public void updateAccommodation(Long accommodationId, UpdateAccommodationRequest updateAccommodationRequest, List<String> existingImageUrls)
        throws IOException {
        accommodationService.updateAccommodation(accommodationId, updateAccommodationRequest);
        postService.updatePost(accommodationId, updateAccommodationRequest);
        accommodationSpaceService.updateAccommodationSpaces(accommodationId, updateAccommodationRequest);
        accommodationAmenityService.updateAccommodationAmenities(accommodationId, updateAccommodationRequest);
        accommodationImageService.updateAccommodationImages(accommodationId, updateAccommodationRequest.getImages(), existingImageUrls);
        accommodationLocationService.updateAccommodationLocation(accommodationId, updateAccommodationRequest);

    }

    @Transactional
    public void deleteAccommodation(Long id) {
        // 방 ID에 해당하는 숙소를 가져오기
        Optional<Accommodation> accommodation = accommodationRepository.findById(id);
        if (accommodation.isPresent()) {
            Long postId = accommodation.get().getPostId(); // 숙소의 post_id 가져오기

            // post 테이블의 is_deleted 값을 1로 변경
            postService.markAsDeleted(postId);
        } else {
            throw new IllegalArgumentException("숙소가 존재하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public AccommodationDetailResponse getAccommodationDetail(Long id) {
        return accommodationDetailCustomRepository.getAccommodationDetailById(id);
    }


    @Transactional(readOnly = true)
    public List<HostManagedAccommodationResponse> getAccommodationsByUserId(Long userId, Long lastId, int limit) {
        return accommodationDetailCustomRepository.findAccommodationsByUserId(userId, lastId, limit);
    }

    @Transactional(readOnly = true)
    public List<HostManagedAccommodationResponse> getAllHostRooms(Long userId) {
        return accommodationDetailCustomRepository.getAllHostRooms(userId);
    }




}
