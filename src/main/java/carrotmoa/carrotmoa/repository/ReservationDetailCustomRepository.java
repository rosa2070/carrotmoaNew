package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.model.response.GuestReservationResponse;
import carrotmoa.carrotmoa.model.response.HostReservationResponse;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationDetailCustomRepository {
    List<GuestReservationResponse> getGuestReservations(Long userId);

    List<HostReservationResponse> getHostReservations(Long hostId);
}
