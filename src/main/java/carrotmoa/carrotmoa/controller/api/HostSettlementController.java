package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.model.response.PaymentDetailResponse;
import carrotmoa.carrotmoa.service.SettlementService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settlement")
public class HostSettlementController {
    private final SettlementService settlementService;

    public HostSettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentDetailResponse>> getSettlement(
        @RequestParam("hostId") Long hostId,
        @RequestParam("accommodationId") Long accommodationId,
        @RequestParam("startDate") LocalDate startDate,
        @RequestParam("endDate") LocalDate endDate) {

        List<PaymentDetailResponse> settlements = settlementService.getSettlementList(hostId, accommodationId, startDate, endDate);
        return ResponseEntity.ok(settlements);

    }


}
