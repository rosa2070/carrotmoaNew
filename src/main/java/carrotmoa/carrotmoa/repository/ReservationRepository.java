package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.Reservation;
import carrotmoa.carrotmoa.model.response.BookingDetailResponse;
import carrotmoa.carrotmoa.model.response.FullCalendarResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r.id, r.accommodationId, r.checkInDate, r.checkOutDate, r.status, r.totalPrice, " +
        "p.title, a.lotAddress, a.detailAddress, a.floor, u.nickname, MIN(i.imageUrl) " +
        "FROM Reservation r " +
        "JOIN Accommodation a ON a.id = r.accommodationId " +
        "JOIN Post p ON a.postId = p.id " +
        "JOIN AccommodationImage i ON a.id = i.accommodationId " +
        "JOIN UserProfile u ON p.userId = u.userId " +
        "WHERE r.userId = :userId " +
        "GROUP BY r.accommodationId, r.checkInDate, r.checkOutDate, r.status, r.totalPrice, " +
        "p.title, a.lotAddress, a.detailAddress, a.floor, u.nickname " +
        "ORDER BY r.createdAt DESC")
    List<Object[]> findBookingData(@Param("userId") Long userId);

    @Query("SELECT new carrotmoa.carrotmoa.model.response.BookingDetailResponse( " +
        "a.id, a.lotAddress, a.detailAddress, a.floor, " +
        "p.title, u.nickname) " +
        "FROM Accommodation a " +
        "JOIN Post p ON p.id = a.postId " +
        "JOIN UserProfile u ON u.userId = p.userId " +
        "WHERE a.id = :accommodationId")
    BookingDetailResponse findBookingDetail(@Param("accommodationId") Long accommodationId);

    // full calendar
    @Query("SELECT new carrotmoa.carrotmoa.model.response.FullCalendarResponse( " +
        "r.checkInDate, r.checkOutDate) " +
        "FROM Reservation r " +
        "WHERE r.accommodationId = :accommodationId AND r.status = 1 ")
    List<FullCalendarResponse> findBookedDates(@Param("accommodationId") Long accommodationId);

    @Modifying // update문에는 @Modigying 필요
    @Transactional
    @Query("UPDATE Reservation r " +
        "SET r.status = 2 " +
        "WHERE r.id = :reservationId")
    void cancelBooking(@Param("reservationId") Long reservationId);

    @Modifying
    @Transactional
    @Query("UPDATE Reservation r " +
        "SET r.status = 3 " +
        "WHERE r.status = 1 AND r.checkOutDate < CURRENT_DATE ")
    void updateBookingStatusIfTimePast();

    @Query("SELECT p.id " +
        "FROM Post p " +
        "JOIN Accommodation a ON a.postId = p.id " +
        "JOIN Reservation r ON r.accommodationId = a.id " +
        "WHERE r.id = :reservationId")
    Long findPostIdByReservationId(@Param("reservationId") Long reservationId);
}
