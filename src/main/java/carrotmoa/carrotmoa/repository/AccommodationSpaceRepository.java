package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.AccommodationSpace;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationSpaceRepository extends JpaRepository<AccommodationSpace, Long> {
    List<AccommodationSpace> findByAccommodationId(Long accommodationId);
}
