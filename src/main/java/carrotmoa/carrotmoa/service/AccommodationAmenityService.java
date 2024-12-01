package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.AccommodationAmenity;
import carrotmoa.carrotmoa.model.request.SaveAccommodationRequest;
import carrotmoa.carrotmoa.model.request.UpdateAccommodationRequest;
import carrotmoa.carrotmoa.repository.AccommodationAmenityRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccommodationAmenityService {
    private final AccommodationAmenityRepository accommodationAmenityRepository;

    public AccommodationAmenityService(AccommodationAmenityRepository accommodationAmenityRepository) {
        this.accommodationAmenityRepository = accommodationAmenityRepository;
    }

    public void saveAccommodationAmenities(Long accommodationId, SaveAccommodationRequest saveAccommodationRequest) {
        if (saveAccommodationRequest.getAmenityIds() != null) {
            List<AccommodationAmenity> accommodationAmenities = saveAccommodationRequest.getAmenityIds().stream()
                .map(amenityId -> AccommodationAmenity.builder()
                    .accommodationId(accommodationId)
                    .amenityId(amenityId)
                    .build())
                .toList();

            accommodationAmenityRepository.saveAll(accommodationAmenities);
        }
    }

    @Transactional
    public void updateAccommodationAmenities(Long accommodationId, UpdateAccommodationRequest updateAccommodationRequest) {
        // 새로운 amenityId 목록을 가져옴
        List<Long> newAmenityIds = updateAccommodationRequest.getAmenityIds();

        // 기존의 편의 시설 목록을 가져옴
        List<AccommodationAmenity> existingAmenities = accommodationAmenityRepository.findByAccommodationId(accommodationId);

        // 기존의 amenityId 목록 생성
        Set<Long> existingAmenityIds = existingAmenities.stream()
            .map(AccommodationAmenity::getAmenityId)
            .collect(Collectors.toSet());

        // 새로 추가할 편의 시설 객체 목록 생성
        List<AccommodationAmenity> amenitiesToAdd = newAmenityIds.stream()
            .filter(amenityId -> !existingAmenityIds.contains(amenityId))
            .map(amenityId -> AccommodationAmenity.builder()
                .accommodationId(accommodationId)
                .amenityId(amenityId)
                .build())
            .toList();

        // 삭제할 amenityId 목록
        List<Long> amenitiesToRemove = existingAmenityIds.stream()
            .filter(amenityId -> !newAmenityIds.contains(amenityId))
            .toList();

        // 새로운 편의 시설 추가
        if (!amenitiesToAdd.isEmpty()) {
            accommodationAmenityRepository.saveAll(amenitiesToAdd);
        }

        // 기존의 편의 시설 삭제
        for (Long amenityId : amenitiesToRemove) {
            accommodationAmenityRepository.deleteByAccommodationIdAndAmenityId(accommodationId, amenityId);
        }

    }
}
