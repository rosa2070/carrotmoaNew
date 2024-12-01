package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.config.security.CustomUserDetails;
import carrotmoa.carrotmoa.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SaveReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/guest/review/save")
    public String saveReview(@RequestParam("reservationId") Long reservationId, @AuthenticationPrincipal CustomUserDetails user,
        @RequestParam("comment") String comment) {
        Long userId = user.getUserProfile().getUserId();
        reviewService.saveReview(reservationId, userId, comment);
        return "redirect:/guest/booking/list";
    }
}
