package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.Review;
import carrotmoa.carrotmoa.model.response.AccommodationReviewResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT new carrotmoa.carrotmoa.model.response.AccommodationReviewResponse( " +
        "r.comment, rs.checkOutDate, u.nickname) " +
        "FROM Review r " +
        "JOIN Accommodation a ON a.postId = r.postId " +
        "JOIN Reservation rs ON a.id = rs.accommodationId " +
        "JOIN UserProfile u ON u.userId = r.userId " +
        "WHERE a.id = :accommodationId AND rs.status = 3 ")
    List<AccommodationReviewResponse> getAccommodationReviews(@Param("accommodationId") Long accommodationId);

    @Query("SELECT r.id " +
        "FROM Review r " +
        "JOIN Accommodation a ON a.postId = r.postId " +
        "JOIN Reservation re ON re.accommodationId = a.id " +
        "JOIN User u ON u.id = r.userId " +
        "WHERE re.id = :reservationId ")
    Long getReviewId(@Param("reservationId") Long reservationId);

}
