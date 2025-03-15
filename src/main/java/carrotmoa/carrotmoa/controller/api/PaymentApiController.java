package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.entity.Payment;
import carrotmoa.carrotmoa.exception.ExternalApiException;
import carrotmoa.carrotmoa.model.request.PaymentAndReservationRequest;
import carrotmoa.carrotmoa.model.request.PaymentRequest;
import carrotmoa.carrotmoa.model.request.ReservationRequest;
import carrotmoa.carrotmoa.service.PaymentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;

@RestController
@RequestMapping("/api/payment")
@Slf4j
public class PaymentApiController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentApiController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/portone")
    public ResponseEntity<String> savePortone(@RequestBody PaymentAndReservationRequest paymentAndReservationRequest) {
        try {
            // 결제 및 예약 정보 처리
            PaymentRequest paymentRequest = paymentAndReservationRequest.getPaymentRequest();
            ReservationRequest reservationRequest = paymentAndReservationRequest.getReservationRequest();

            // Payment 처리 후, Reservation 처리
            paymentService.processPaymentAndReservation(paymentRequest, reservationRequest);
            return ResponseEntity.ok("Payment processed successfully");
        } catch (ExternalApiException e){
            // 재시도 후에도 실패시 500 Inernal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process payment: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process payment.");
        }
    }


    /**
     * 모든 결제 내역 조회
     *
     * @return 모든 Payment 엔티티 리스트
     */
    @GetMapping("/list")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @DeleteMapping("/cancel/{uid}")
    public ResponseEntity<Map<String, String>> cancelPayment(@PathVariable("uid") String uid) {
        Map<String, String> response = new HashMap<>();
        try {
            paymentService.cancelPayment(uid);
            response.put("message", "결제가 취소되었습니다.");
            return ResponseEntity.ok(response);
        } catch (HttpClientErrorException e) {
            response.put("error", "잘못된 요청: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (UnknownHttpStatusCodeException e) {
            response.put("error", "알 수 없는 상태 코드: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (ResourceAccessException | HttpServerErrorException e) {
            response.put("error", "서버 문제로 인해 일시적으로 사용할 수 없습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        } catch (RestClientException e) {
            response.put("error", "결제 취소 중 알 수 없는 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
