package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.entity.Payment;
import carrotmoa.carrotmoa.exception.ExternalApiException;
import carrotmoa.carrotmoa.model.request.PaymentAndReservationRequest;
import carrotmoa.carrotmoa.model.request.PaymentRequest;
import carrotmoa.carrotmoa.model.request.ReservationRequest;
import carrotmoa.carrotmoa.service.PaymentService;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
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

    @GetMapping("/cancel/{uid}")
    public ResponseEntity<String> cancelPayment(@PathVariable("uid") String uid) {
        try {
            paymentService.cancelPayment(uid);  // 결제 취소 요청
            return ResponseEntity.ok("결제 취소가 성공적으로 완료되었습니다.");
        } catch (ExternalApiException e) {
            // 서비스에서 던진 예외를 받아서 클라이언트에게 응답
            log.error("Payment cancellation failed for impUid: {}. Error: {}", uid, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());  // 예외 메시지 그대로 반환
//        } catch (IllegalArgumentException e) {
//            log.error("Invalid payment information or already cancelled payment for impUid: {}", uid);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body("결제 정보가 없거나 이미 취소된 결제입니다. 결제 상태를 확인해주세요.");
        } catch (Exception e) {
            log.error("Unexpected error occurred while cancelling payment for impUid: {}. Error: {}", uid, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }
}
