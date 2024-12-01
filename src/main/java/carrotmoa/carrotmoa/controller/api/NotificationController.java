package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.model.request.NotificationUpdateRequest;
import carrotmoa.carrotmoa.model.response.NotificationResponse;
import carrotmoa.carrotmoa.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 1. SSE 연결하는 API
    @GetMapping(value = "/sse/notifications/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@PathVariable("userId") Long userId) {
        System.out.println(userId);
        return ResponseEntity.ok(notificationService.subscribe(userId));
    }

    // 2. DB에서 수신자 아이디로 알림 리스트 가져오는 API
    @GetMapping("/api/notifications/{receiverId}")
    public ResponseEntity<List<NotificationResponse>> findNotificationsByReceiverId(
        @PathVariable("receiverId") Long receiverId) {
        List<NotificationResponse> notifications = notificationService.findNotificationsByReceiverId(receiverId);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/api/notifications")
    public ResponseEntity<List<Long>> updateNotifications(@RequestBody List<NotificationUpdateRequest> updateRequests) {
        System.out.println("Received updateRequests: " + updateRequests); // 추가된 로그
        List<Long> updatedNotificationIds = notificationService.updateNotifications(updateRequests);
        System.out.println("Updated Notification IDs: " + updatedNotificationIds);
        return ResponseEntity.ok(updatedNotificationIds); // 업데이트된 알림 반환
    }
}