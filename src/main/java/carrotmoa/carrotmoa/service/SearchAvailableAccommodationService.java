package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.model.response.AccommodationAvailableResponse;
import carrotmoa.carrotmoa.repository.AccommodationRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SearchAvailableAccommodationService {
    @Autowired
    AccommodationRepository accommodationRepository;

    @Transactional(readOnly = true)
    public List<AccommodationAvailableResponse> getAllAvailableAccommodations() {
        List<Object[]> list = accommodationRepository.findAllAvailableAccommodations();
        return list.stream()
            .map(AccommodationAvailableResponse::fromData)
            .collect(Collectors.toList());
    }
}
