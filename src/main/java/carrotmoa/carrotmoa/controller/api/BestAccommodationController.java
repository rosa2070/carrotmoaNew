package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.model.response.BestAccommodationResponse;
import carrotmoa.carrotmoa.service.BestAccommodationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
public class BestAccommodationController {

    private final BestAccommodationService bestAccommodationService;

    public BestAccommodationController(BestAccommodationService bestAccommodationService) {
        this.bestAccommodationService = bestAccommodationService;
    }

    @GetMapping("/best-room")
    public ResponseEntity<List<BestAccommodationResponse>> getBestAccommodations() {
        List<BestAccommodationResponse> bestAccommodations = bestAccommodationService.getBestAccommodationsFromRds();
        return ResponseEntity.ok(bestAccommodations);
//                .contentType(MediaType.APPLICATION_JSON) // Content-Type을 application/json으로 명시
//                .body(bestAccommodations);
    }

    // 인기 숙소 목록을 가져오는 API
    @GetMapping("/best")
    public ResponseEntity<List<BestAccommodationResponse>> getBestAccommodations_withRedis() {
        // Redis에서 인기 숙소 데이터를 가져옵니다.
        List<BestAccommodationResponse> bestAccommodations = bestAccommodationService.getBestAccommodationsFromRedis();

        // 데이터가 정상적으로 조회되었으면 200 OK로 응답
        return ResponseEntity.ok(bestAccommodations);
    }

}
