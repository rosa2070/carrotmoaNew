package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.model.response.AccommodationImageResponse;
import carrotmoa.carrotmoa.model.response.BookingDetailResponse;
import carrotmoa.carrotmoa.model.response.FullCalendarResponse;
import carrotmoa.carrotmoa.model.response.GuestReservationResponse;
import carrotmoa.carrotmoa.repository.AccommodationImageRepository;
import carrotmoa.carrotmoa.repository.ReservationDetailCustomRepository;
import carrotmoa.carrotmoa.repository.ReservationRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final AccommodationImageRepository accommodationImageRepository;
    private final ReservationDetailCustomRepository reservationDetailCustomRepository;

    public ReservationService(ReservationRepository reservationRepository,
        AccommodationImageRepository accommodationImageRepository,
        ReservationDetailCustomRepository reservationDetailCustomRepository) {
        this.reservationRepository = reservationRepository;
        this.accommodationImageRepository = accommodationImageRepository;
        this.reservationDetailCustomRepository = reservationDetailCustomRepository;
    }

    @Transactional(readOnly = true)
    // 게스트 예약 확인하는 페이지에서 사용
    // guest/booking/list/{id}
    public List<GuestReservationResponse> getBookingList(Long id) {
        return reservationDetailCustomRepository.getGuestReservations(id);
    }

    @Transactional(readOnly = true)
    public BookingDetailResponse getBookingDetail(Long id) {
        return reservationRepository.findBookingDetail(id);
    }

    @Transactional(readOnly = true)
    public List<FullCalendarResponse> getBookedDates(Long id) {
        return reservationRepository.findBookedDates(id);
    }

    @Transactional(readOnly = true)
    public List<AccommodationImageResponse> getAccommodationImageByUserId(Long id) {
        List<Object[]> images = accommodationImageRepository.findByUserId(id);
        return images.stream()
            .map(AccommodationImageResponse::fromData)
            .collect(Collectors.toList());
    }
}
