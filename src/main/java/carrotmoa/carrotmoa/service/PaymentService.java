package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.Accommodation;
import carrotmoa.carrotmoa.entity.Payment;
import carrotmoa.carrotmoa.entity.Post;
import carrotmoa.carrotmoa.entity.Reservation;
import carrotmoa.carrotmoa.entity.UserProfile;
import carrotmoa.carrotmoa.enums.NotificationType;
import carrotmoa.carrotmoa.exception.ExternalApiException;
import carrotmoa.carrotmoa.model.request.PaymentRequest;
import carrotmoa.carrotmoa.model.request.ReservationRequest;
import carrotmoa.carrotmoa.model.request.SaveNotificationRequest;
import carrotmoa.carrotmoa.repository.AccommodationRepository;
import carrotmoa.carrotmoa.repository.PaymentRepository;
import carrotmoa.carrotmoa.repository.PostRepository;
import carrotmoa.carrotmoa.repository.ReservationRepository;
import carrotmoa.carrotmoa.repository.UserProfileRepository;
import carrotmoa.carrotmoa.util.PaymentClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentClient paymentClient;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService; // NotificationService 추가
    private final AccommodationRepository accommodationRepository;
    private final PostRepository postRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          PaymentClient paymentClient,
                          ReservationRepository reservationRepository,
                          NotificationService notificationService,
                          AccommodationRepository accommodationRepository,
                          PostRepository postRepository,
                          UserProfileRepository userProfileRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentClient = paymentClient;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
        this.accommodationRepository = accommodationRepository;
        this.postRepository = postRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public void processPaymentAndReservation(PaymentRequest paymentRequest, ReservationRequest reservationRequest) {
        // 결제 정보 저장
        Payment payment = savePayment(paymentRequest);

        // 상태가 "paid"인 경우에만 예약 저장
        if ("paid".equals(payment.getStatus())) {
            Reservation reservation = saveReservation(reservationRequest);
            payment.setReservationId(reservation.getId());
//            paymentRepository.save(payment);

            // 계약하려는 방 호스트의 ID 받아오기
            Accommodation accommodation = accommodationRepository.findById(reservation.getAccommodationId())
                    .orElseThrow(() -> new EntityNotFoundException("Accommodation not found"));

            Post post = postRepository.findById(accommodation.getPostId())
                    .orElseThrow(() -> new EntityNotFoundException("Post not found"));

            Long receiverId = post.getUserId();
            String roomName = post.getTitle();

            if (!reservationRequest.getUserId().equals(receiverId)) {
                String notificationUrl = "/host/room/contract";
                String message = roomName + "방을 예약했어요";
                Long senderId = reservationRequest.getUserId();
                NotificationType notificationType = NotificationType.RESERVATION_CONFIRM;

                notificationService.sendReservationNotification(notificationType, senderId, receiverId, notificationUrl, message);
                notificationService.sendReservationNotification(notificationType, senderId, senderId, notificationUrl, message); // 변수명 게스트 호스트로 변경하는게 나을 거 같음
            }

        }


    }


    public Payment savePayment(PaymentRequest paymentRequest) {
        Payment payment = paymentRequest.toPaymentEntity();
        return paymentRepository.save(payment);
    }

    public Reservation saveReservation(ReservationRequest reservationRequest) {
        Reservation reservation = Reservation.builder()
                .accommodationId(reservationRequest.getAccommodationId())
                .userId(reservationRequest.getUserId())
                .checkInDate(reservationRequest.getCheckInDate())
                .checkOutDate(reservationRequest.getCheckOutDate())
                .totalPrice(reservationRequest.getTotalPrice())
                .status(reservationRequest.getStatus())
                .build();

        return reservationRepository.save(reservation);
    }

    /**
     * 모든 결제 내역 조회
     *
     * @return 모든 Payment 엔티티 리스트
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }


    /**
     * 결제 내역 삭제
     *
     * @param uid 포트원 거래고유번호
     */
    @Transactional
    public void cancelPayment(String uid) {
        try {
            paymentClient.cancelPayment(uid);  // API 호출 (실패 시 예외 발생)

            // impUid로 Payment 엔티티 조회
            Payment payment = paymentRepository.findByImpUid(uid)
                    .orElseThrow(() -> new IllegalArgumentException("Payment not found with impUid: " + uid));

            // Payment의 status 필드를 "cancel"로 변경
            payment.setStatus("cancel");

            // 예약 정보 조회 및 상태 변경
            if (payment.getReservationId() != null) {
                Reservation reservation = reservationRepository.findById(payment.getReservationId())
                        .orElseThrow(() -> new IllegalArgumentException("Reservation not found with id: " + payment.getReservationId()));

                // 예약 상태를 변경 (예약 취소: 2)
                reservation.setStatus(2);

                // 알림 발송
                Long senderId = payment.getUserId();
                Long receiverId = payment.getUserId();
                String notificationUrl = "/guest/booking/list";
                String message = "결제를 취소했어요";
                NotificationType notificationType = NotificationType.GUEST_CANCEL;

                notificationService.sendReservationNotification(notificationType, senderId, receiverId, notificationUrl, message);
            }
        } catch (RestClientException e) {  // `PaymentClient`에서 던진 예외를 잡음
            log.error("Payment cancellation failed: {}", e.getMessage());
            throw e;  // 예외를 다시 던져서 트랜잭션을 롤백
        }

    }








}
