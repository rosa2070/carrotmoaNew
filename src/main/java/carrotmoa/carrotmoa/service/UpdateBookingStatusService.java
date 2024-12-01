package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UpdateBookingStatusService {
    @Autowired
    private ReservationRepository reservationRepository;

    // 날짜 바뀌는 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void updateStatusIfTimePast() {
        reservationRepository.updateBookingStatusIfTimePast();
    }
}
