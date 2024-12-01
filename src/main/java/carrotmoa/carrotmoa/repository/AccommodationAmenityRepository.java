package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.AccommodationAmenity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationAmenityRepository extends JpaRepository<AccommodationAmenity, Long> {

    List<AccommodationAmenity> findByAccommodationId(Long accommodationId);

    void deleteByAccommodationIdAndAmenityId(Long accommodationId, Long amenityId);

    @Query("SELECT am.id, am.name, am.iconUrl " +
        "FROM Accommodation a " +
        "JOIN AccommodationAmenity aa ON a.id = aa.accommodationId " +
        "JOIN Amenity am ON aa.amenityId = am.id " +
        "WHERE aa.accommodationId = :accommodationId")
    List<Object[]> findAccommodationAmenitiesByAccommodationId(@Param("accommodationId") Long accommodationId);

}
