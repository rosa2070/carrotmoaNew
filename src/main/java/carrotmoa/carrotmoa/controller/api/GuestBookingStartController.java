package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.config.security.CustomUserDetails;
import carrotmoa.carrotmoa.model.response.BookingDetailResponse;
import carrotmoa.carrotmoa.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//@RestController
@Controller
@RequestMapping("/guest/booking/start")
public class GuestBookingStartController {
    @Autowired
    ReservationService reservationService;

//    @GetMapping("/{id}") // postman
//    public BookingDetailResponse getBookingDetail(@PathVariable("id") Long id) {
//        BookingDetailResponse bookingDetail = reservationService.getBookingDetail(id);
//        return bookingDetail;
//    }

//    @GetMapping("/{id}")
//    public String getBookingDetail(@PathVariable("id") Long id, Model model) {
//        BookingDetailResponse bookingDetail = reservationService.getBookingDetail(id);
//        model.addAttribute("booking", bookingDetail);
//        return "guest/bookingStart";
//    }

    @GetMapping("/{id}")
    public String getBookingDetail(@ModelAttribute("user") CustomUserDetails user, @PathVariable("id") Long id, Model model) {
        if (user == null) {
            return "user/login-page";
        }
        BookingDetailResponse bookingDetail = reservationService.getBookingDetail(id);
        model.addAttribute("booking", bookingDetail);
        model.addAttribute("user", user);
        return "guest/bookingStart";
    }


}
