package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.entity.Payment;
import carrotmoa.carrotmoa.model.request.PaymentAndReservationRequest;
import carrotmoa.carrotmoa.service.PaymentService;
import java.util.List;
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
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

//    @PostMapping("/portone")
//    public ResponseEntity<?> savePortone(@RequestBody PaymentRequest paymentRequest) {
//        try {
//            paymentService.savePayment(paymentRequest.toPaymentEntity());
//            return ResponseEntity.ok(new SuccessResponse("Payment processed successfully."));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ErrorResponse("Failed to process payment.", HttpStatus.INTERNAL_SERVER_ERROR.value()));        }
//    }

//    @PostMapping("/portone")
//    public ResponseEntity<String> savePortone(@RequestBody PaymentRequest paymentRequest) {
//        try {
//            paymentService.savePayment(paymentRequest.toPaymentEntity());
//            return ResponseEntity.ok("Payment processed successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process payment.");
//        }
//    }

    @PostMapping("/portone")
    public ResponseEntity<String> savePortone(@RequestBody PaymentAndReservationRequest paymentAndReservationRequest) {
        try {
            // 결제 및 예약 정보 처리
            paymentService.processPaymentAndReservation(paymentAndReservationRequest.getPaymentRequest(),
                paymentAndReservationRequest.getReservationRequest());
            return ResponseEntity.ok("Payment processed successfully");
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
        paymentService.cancelPayment(uid);
        return ResponseEntity.ok("결제가 성공적으로 완료되었습니다.");
    }
}
