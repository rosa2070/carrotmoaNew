package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.Accommodation;
import carrotmoa.carrotmoa.model.request.SaveAccommodationRequest;
import carrotmoa.carrotmoa.model.request.UpdateAccommodationRequest;
import carrotmoa.carrotmoa.repository.AccommodationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;

    public AccommodationService(AccommodationRepository accommodationRepository) {
        this.accommodationRepository = accommodationRepository;
    }

    @Transactional
    public Accommodation saveAccommodation(SaveAccommodationRequest saveAccommodationRequest, Long postId) {

        Accommodation accommodation = Accommodation.builder()
            .postId(postId)
            .totalArea(saveAccommodationRequest.getTotalArea())
            .roadAddress(saveAccommodationRequest.getRoadAddress())
            .lotAddress(saveAccommodationRequest.getLotAddress())
            .detailAddress(saveAccommodationRequest.getDetailAddress())
            .floor(saveAccommodationRequest.getFloor())
            .totalFloor(saveAccommodationRequest.getTotalFloor())
            .price(saveAccommodationRequest.getPrice())
            .transportationInfo(saveAccommodationRequest.getTransportationInfo())
            .build();

        return accommodationRepository.save(accommodation);
    }

    @Transactional
    public void updateAccommodation(Long accommodationId, UpdateAccommodationRequest updateRequest) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new IllegalArgumentException("숙소를 찾을 수 없습니다."));

        accommodation.updateAccommodation(
            updateRequest.getTotalArea(),
            updateRequest.getRoadAddress(),
            updateRequest.getLotAddress(),
            updateRequest.getDetailAddress(),
            updateRequest.getFloor(),
            updateRequest.getTotalFloor(),
            updateRequest.getPrice(),
            updateRequest.getTransportationInfo()
        );
    }
}
