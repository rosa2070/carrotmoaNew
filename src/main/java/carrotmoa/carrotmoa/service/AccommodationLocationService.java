package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.Accommodation;
import carrotmoa.carrotmoa.entity.AccommodationLocation;
import carrotmoa.carrotmoa.model.request.SaveAccommodationRequest;
import carrotmoa.carrotmoa.model.request.UpdateAccommodationRequest;
import carrotmoa.carrotmoa.repository.AccommodationLocationRepository;
import carrotmoa.carrotmoa.repository.AccommodationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccommodationLocationService {
    private final AccommodationLocationRepository accommodationLocationRepository;
    private final AccommodationRepository accommodationRepository;

    public AccommodationLocationService(AccommodationLocationRepository accommodationLocationRepository,
        AccommodationRepository accommodationRepository) {
        this.accommodationLocationRepository = accommodationLocationRepository;
        this.accommodationRepository = accommodationRepository;
    }

    @Transactional
    public void saveAccommodationLocation(Long accommodationId, SaveAccommodationRequest saveAccommodationRequest) {
        AccommodationLocation accommodationLocation = AccommodationLocation.builder()
            .accommodationId(accommodationId)
            .longitude(saveAccommodationRequest.getLongitude())
            .latitude(saveAccommodationRequest.getLatitude())
            .build();
        accommodationLocationRepository.save(accommodationLocation);
    }

    public void updateAccommodationLocation(Long accommodationId, UpdateAccommodationRequest updateAccommodationRequest) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new IllegalArgumentException("숙소를 찾을 수 없습니다."));

        AccommodationLocation accommodationLocation = accommodationLocationRepository.findByAccommodationId(accommodation.getId())
            .orElseThrow(() -> new IllegalArgumentException("좌표를 찾을 수 없습니다."));

        accommodationLocation.updateAccommodationLocation(updateAccommodationRequest.getLatitude(), updateAccommodationRequest.getLongitude());


    }

}
