package carrotmoa.carrotmoa.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchMapController {
    @GetMapping("/search/map")
    public String gotoSearchMap() {
//        return "kakaomap/kakaomap-accommodation";
        return "guest/roomMap";
    }
}
