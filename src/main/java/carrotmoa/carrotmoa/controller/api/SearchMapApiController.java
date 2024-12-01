package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.model.response.AccommodationAvailableResponse;
import carrotmoa.carrotmoa.service.SearchAvailableAccommodationService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
//@Controller
@RestController
@RequestMapping("/api/accommodation-data")
public class SearchMapApiController {
    @Autowired
    SearchAvailableAccommodationService searchAvailableAccommodationService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllAccommodationData() {
        List<AccommodationAvailableResponse> list = searchAvailableAccommodationService.getAllAvailableAccommodations();
        Map<String, Object> map = new HashMap<>();
        map.put("accommodations", list);
        return ResponseEntity.ok(map);
    }
}
