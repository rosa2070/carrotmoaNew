package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guest/booking/cancel")
public class CancelBookingApiController {
    @Autowired
    ReservationRepository reservationRepository;

    @GetMapping("/{reservationId}")
    public String cacelBooking(@PathVariable("reservationId") Long reservationId) {
        reservationRepository.cancelBooking(reservationId);
        return "guest/bookingList";
    }
}
