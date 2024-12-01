package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.Notification;
import carrotmoa.carrotmoa.entity.Payment;
import carrotmoa.carrotmoa.entity.UserProfile;
import carrotmoa.carrotmoa.enums.NotificationType;
import carrotmoa.carrotmoa.model.request.NotificationUpdateRequest;
import carrotmoa.carrotmoa.model.request.ReservationRequest;
import carrotmoa.carrotmoa.model.request.SaveNotificationRequest;
import carrotmoa.carrotmoa.model.response.NotificationResponse;
import carrotmoa.carrotmoa.model.response.SseNotificationResponse;
import carrotmoa.carrotmoa.repository.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static carrotmoa.carrotmoa.entity.QPayment.payment;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 600L * 1000 * 60; // 연결 시간 10분
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final PaymentRepository paymentRepository;
    private final UserProfileRepository userProfileRepository;

    // 계속 생성안되게 검증 로직 추가하기.
    public SseEmitter subscribe(Long userId) {
//        if (emitterRepository.existsById(userId)) {
//            return emitterRepository.getById(userId); // 기존 Emitter 반환
//        }
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));
        try {
            emitter.send(SseEmitter.event()
                .name("connect") // 이벤트 이름 지정
                .data("connected!")); // 503에러 방지를 위한 더미 데이터
            emitterRepository.save(userId, emitter);
            log.info("SSE 객체가 Map에 잘 저장되었을까요? -> {}", emitterRepository.get(userId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return emitter;
    }


    // 여기서는 SSE 실시간 알림 발송(1) + 알림 테이블에 DB 적재(2)를 한 번에 하는 메서드
    @Transactional
    public void sendNotification(Long receiverId, SaveNotificationRequest saveNotificationRequest, String userName, String picUrl) {
        Notification notification = saveNotification(saveNotificationRequest);

        SseNotificationResponse sseNotificationResponse = new SseNotificationResponse(
            notification.getId(),
            NotificationType.COMMENT.getTitle(),
            userName,
            picUrl, saveNotificationRequest.getMessage(),
            saveNotificationRequest.getUrl(),
            notification.isRead(),
            notification.isDeleted(),
            notification.getCreatedAt());
        sendSseNotification(receiverId, sseNotificationResponse);
    }

    // 1. 알림 저장 메서드
    private Notification saveNotification(SaveNotificationRequest saveNotificationRequest) {
        return notificationRepository.save(saveNotificationRequest.toNotificationEntity());
    }

    // 2. SSE로 알림 전송하는 메서드
    private void sendSseNotification(Long receiverId, SseNotificationResponse sseNotificationResponse) {
        SseEmitter emitter = emitterRepository.get(receiverId);
        if (emitter != null) {
            try {
                // SSE 전송 로직
                emitter.send(SseEmitter.event().data(sseNotificationResponse));
            } catch (Exception e) {
                emitterRepository.deleteById(receiverId);  // SSE 전송 실패 시 Emitter 삭제
            }
        }
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> findNotificationsByReceiverId(Long receiverId) {
        return notificationRepository.findNotificationsByReceiverId(receiverId);
    }


    @Transactional
    public List<Long> updateNotifications(List<NotificationUpdateRequest> updateRequests) {
        // 요청에 따라 알림을 업데이트
        List<Long> updatedNotificationIds = updateRequests.stream()
            .map(request -> {
                // 알림을 ID로 조회
                Notification notification = notificationRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("해당 알림의 아이디를 찾을 수 없습니다: " + request.getId()));

                // 상태 업데이트
                notification.updateStatus(request.isRead(), request.isDeleted());

                return notification.getId(); // 업데이트된 알림 ID 반환
            })
            .collect(Collectors.toList());

        // saveAll을 사용하여 일괄 업데이트 (JPA는 변경 감지를 기반으로 함)
        notificationRepository.saveAll(updatedNotificationIds.stream()
            .map(id -> notificationRepository.getById(id))
            .collect(Collectors.toList()));

        return updatedNotificationIds;
    }

//    public void sendCancelReservationNotification(Long userId, String notificationUrl) {
//        SaveNotificationRequest saveNotificationRequest = new SaveNotificationRequest(
//                NotificationType.RESERVATION_CONFIRM,
//                userId,
//                userId,
//                "결제가 성공적으로 취소되었습니다.",
//                notificationUrl
//        );
//        UserProfile senderUser = userProfileRepository.findNicknameByUserId(userId);
//        sendNotification(userId,saveNotificationRequest, senderUser.getNickname(), senderUser.getPicUrl());
//    }

    public void sendReservationNotification(NotificationType notificationType, Long senderId, Long receiverId, String notificationUrl, String message) {
        SaveNotificationRequest saveNotificationRequest = new SaveNotificationRequest(
                notificationType,
                receiverId,
                senderId,
                message,
                notificationUrl
        );
        UserProfile senderUser = userProfileRepository.findNicknameByUserId(senderId);
        sendNotification(receiverId,saveNotificationRequest, senderUser.getNickname(), senderUser.getPicUrl());
    }
}




