package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.service.PopularRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/popular-rooms")
public class PopularRoomController {

    private final PopularRoomService popularRoomService;

    @Autowired
    public PopularRoomController(PopularRoomService popularRoomService) {
        this.popularRoomService = popularRoomService;
    }

    // 인기 호스트 방 목록을 반환하는 API


}
