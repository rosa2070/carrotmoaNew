package carrotmoa.carrotmoa.controller.view;

import carrotmoa.carrotmoa.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/guest/review")
public class ReviewViewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{reservationId}")
    public String gotoReviewView(@PathVariable("reservationId") Long reservationId, RedirectAttributes redirectAttributes) {
        // 리뷰가 존재하는지 확인
        if (reviewService.getReservationId(reservationId) != null) {
            redirectAttributes.addFlashAttribute("error", "이미 리뷰를 작성하셨습니다.");
            return "redirect:/guest/booking/list";
        }
        return "guest/review";
    }
}
