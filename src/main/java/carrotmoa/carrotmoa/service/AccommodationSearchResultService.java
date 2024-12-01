package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.model.response.AccommodationResultResponse;
import carrotmoa.carrotmoa.repository.AccommodationRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AccommodationSearchResultService {
    // 생성자 주입방식
    private final AccommodationRepository accommodationRepository;

    public AccommodationSearchResultService(AccommodationRepository accommodationRepository) {
        this.accommodationRepository = accommodationRepository;
    }

    @Transactional(readOnly = true)
    public List<AccommodationResultResponse> searchAccommodations(String keyword) {
        List<Object[]> results = accommodationRepository.searchAccommodationByKeyword(keyword);
        return results.stream()
            .map(AccommodationResultResponse::fromData)
            .collect(Collectors.toList());
    }


}