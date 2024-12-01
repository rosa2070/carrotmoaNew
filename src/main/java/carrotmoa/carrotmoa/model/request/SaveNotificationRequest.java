package carrotmoa.carrotmoa.model.request;

import carrotmoa.carrotmoa.entity.Notification;
import carrotmoa.carrotmoa.enums.NotificationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SaveNotificationRequest {
    private NotificationType type;
    private Long receiverId;
    private Long senderId;
    private String message;
    private String url;
    private boolean isRead;
    private boolean isDeleted;

    public SaveNotificationRequest(NotificationType type, Long receiverId, Long senderId, String message, String url) {
        this.type = type;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.message = message;
        this.url = url;
        this.isRead = false;
        this.isDeleted = false;
    }

    public Notification toNotificationEntity() {
        return Notification.builder()
            .typeId(type.getTypeId())
            .receiverId(receiverId)
            .senderId(senderId)
            .message(message)
            .url(url)
            .isRead(isRead)
            .isDeleted(isDeleted)
            .build();
    }
}
