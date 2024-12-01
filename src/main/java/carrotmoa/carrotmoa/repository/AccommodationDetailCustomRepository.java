package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.model.response.AccommodationDetailResponse;
import carrotmoa.carrotmoa.model.response.HostManagedAccommodationResponse;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationDetailCustomRepository {
    AccommodationDetailResponse getAccommodationDetailById(Long id);

    List<HostManagedAccommodationResponse> findAccommodationsByUserId(Long userId, Long lastId, int limit);

    List<HostManagedAccommodationResponse> getAllHostRooms(Long userId);
}