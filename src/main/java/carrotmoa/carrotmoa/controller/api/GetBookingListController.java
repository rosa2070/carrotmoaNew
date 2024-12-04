package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.config.security.CustomUserDetails;
import carrotmoa.carrotmoa.model.response.GuestReservationResponse;
import carrotmoa.carrotmoa.service.ReservationService;
import java.util.List;
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

//    @GetMapping("/{id}") // postman test
//    public List<BookingListResponse> getList(@PathVariable("id") Long id) {
//        List<BookingListResponse> bookings = reservationService.getBookingList(id);;
//        return bookings;
//    }

    @GetMapping
    public String getBookingList(@ModelAttribute("user") CustomUserDetails user, Model model) {
        if (user == null) {
            return "user/login-page";
        }

        Long userId = user.getUserProfile().getUserId();
        List<GuestReservationResponse> bookings = reservationService.getBookingList(userId); // userId
//        List<AccommodationImageResponse> image  = reservationService.getAccommodationImageByUserId(id);

        model.addAttribute("bookings", bookings);
//        model.addAttribute("image", image);
        return "guest/bookingList";
    }
}
