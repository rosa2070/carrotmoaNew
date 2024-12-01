package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.model.response.AccommodationResultResponse;
import carrotmoa.carrotmoa.service.AccommodationSearchResultService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@RestController
@Controller
@RequestMapping("/guest")
@Slf4j
public class GuestRoomSearchResultController {

    @Autowired
    AccommodationSearchResultService accommodationSearchResultService;

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<AccommodationResultResponse> rooms = accommodationSearchResultService.searchAccommodations(keyword);
        model.addAttribute("rooms", rooms);
        model.addAttribute("keyword", keyword);
        return "guest/roomResult";
    }

}
