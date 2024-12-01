package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.AccommodationImage;
import carrotmoa.carrotmoa.model.response.AccommodationImageListResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccommodationImageRepository extends JpaRepository<AccommodationImage, Long> {
    List<AccommodationImageListResponse> findByAccommodationId(@Param("accommodationId") Long accommodationId);

    void deleteByImageUrl(String imageUrl);

//    @Query(value = "SELECT i.accommodationId, i.imageUrl " +
//            "FROM AccommodationImage i " +
//            "JOIN Accommodation a ON a.id = i.accommodationId " +
//            "JOIN Post p ON p.id = a.postId " +
//            "WHERE p.userId = :userId " +
//            "ORDER BY i.imageOrder ASC LIMIT 1",
//    nativeQuery = true)
//    List<Object[]> findImageByUserId(@Param("id") Long id);

    @Query("SELECT r.accommodationId, MIN(i.imageUrl)  " +
        "FROM Reservation r " +
        "JOIN AccommodationImage i ON r.accommodationId  = i.accommodationId " +
        "WHERE r.userId = :userId " +
        "GROUP BY r.accommodationId")
        // group by 쓰면 원래 형태가 아닌 string 형태로 변환
    List<Object[]> findByUserId(@Param("userId") Long userId);
}
