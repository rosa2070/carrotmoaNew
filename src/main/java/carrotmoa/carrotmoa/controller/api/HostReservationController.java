package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.model.response.HostReservationResponse;
import carrotmoa.carrotmoa.service.HostReservationService;
import java.util.Collections;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/host/reservation")
public class HostReservationController {
    private final HostReservationService hostReservationService;

    public HostReservationController(HostReservationService hostReservationService) {
        this.hostReservationService = hostReservationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<HostReservationResponse>> getHostReservations(@PathVariable("userId") Long userId) {
        List<HostReservationResponse> hostReservations = hostReservationService.getHostReservations(userId);

        if (hostReservations.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); // 200 OK와 빈 배열 반환
        }

        return ResponseEntity.ok(hostReservations);
    }

}
