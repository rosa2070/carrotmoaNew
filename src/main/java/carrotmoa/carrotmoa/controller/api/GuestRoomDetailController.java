package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.config.security.CustomUserDetails;
import carrotmoa.carrotmoa.model.response.AccommodationDetailResponse;
import carrotmoa.carrotmoa.model.response.AccommodationReviewResponse;
import carrotmoa.carrotmoa.model.response.AmenityImageResponse;
import carrotmoa.carrotmoa.model.response.SpaceImageResponse;
import carrotmoa.carrotmoa.model.response.UserProfileResponse;
import carrotmoa.carrotmoa.service.GuestRoomDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//@RestController
@Controller
@RequestMapping("/room/detail")
public class GuestRoomDetailController {
    @Autowired
    GuestRoomDetailService guestRoomDetailService;


    @GetMapping("/{id}")
    public String roomDetail(@ModelAttribute("user") CustomUserDetails user, @PathVariable("id") Long id, Model model) {
        AccommodationDetailResponse getRoomDetailById = guestRoomDetailService.getAccommodationDetail(id);
        List<AmenityImageResponse> amenities = guestRoomDetailService.getAmenityImage(id);
        List<SpaceImageResponse> icons = guestRoomDetailService.getSpaceImage();
        List<UserProfileResponse> profile = guestRoomDetailService.getHostProfile(id);
        List<AccommodationReviewResponse> reviews = guestRoomDetailService.getAllReview(id);
        int reviewCount = reviews.size();
        System.out.println(reviews);
        model.addAttribute("room", getRoomDetailById);
        model.addAttribute("amenities", amenities);
        model.addAttribute("icons", icons);
        model.addAttribute("profile", profile);
        model.addAttribute("reviews", reviews);
        model.addAttribute("reviewCount", reviewCount);

        // 링크 복사를 위해서 model에 담아주기
        String copyLinkUrl = "http://localhost:8080/room/detail/" + id;
        model.addAttribute("copyLinkUrl", copyLinkUrl);
        return "guest/roomDetail";
    }


}
