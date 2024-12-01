package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.model.response.HostReservationResponse;
import carrotmoa.carrotmoa.repository.ReservationDetailCustomRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HostReservationService {

    private final ReservationDetailCustomRepository reservationDetailCustomRepository;

    public HostReservationService(ReservationDetailCustomRepository reservationDetailCustomRepository) {
        this.reservationDetailCustomRepository = reservationDetailCustomRepository;
    }

    @Transactional(readOnly = true)
    public List<HostReservationResponse> getHostReservations(Long hostId) {
        return reservationDetailCustomRepository.getHostReservations(hostId);
    }
}
