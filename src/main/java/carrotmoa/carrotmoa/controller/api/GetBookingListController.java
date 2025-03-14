package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.config.security.CustomUserDetails;
import carrotmoa.carrotmoa.model.response.GuestReservationResponse;
import carrotmoa.carrotmoa.service.ReservationService;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RestController
@RequestMapping("/guest/booking/list")
public class GetBookingListController {
    @Autowired
    ReservationService reservationService;

    @GetMapping
    public String getBookingList(@ModelAttribute("user") CustomUserDetails user, Model model) {
        if (user == null) {
            return "user/login-page";
        }

        Long userId = user.getUserProfile().getUserId();
        List<GuestReservationResponse> bookings = reservationService.getBookingList(userId); // userId

//        model.addAttribute("bookings", bookings);

        // 상태별로 필터링하여 모델에 추가
        model.addAttribute("completedBookings",
                bookings.stream().filter(b -> b.getStatus() == 1).collect(Collectors.toList()));

        model.addAttribute("expiredBookings",
                bookings.stream().filter(b -> b.getStatus() == 3).collect(Collectors.toList()));

        model.addAttribute("canceledBookings",
                bookings.stream().filter(b -> b.getStatus() == 2).collect(Collectors.toList()));


        return "guest/bookingList";
    }
}
