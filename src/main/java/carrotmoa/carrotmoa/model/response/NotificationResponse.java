package carrotmoa.carrotmoa.model.response;

import carrotmoa.carrotmoa.entity.Notification;
import carrotmoa.carrotmoa.entity.NotificationType;
import carrotmoa.carrotmoa.entity.UserProfile;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private String title; // notification_type 테이블의 title -> join
    private String userName;
    private String picUrl;
    private String message; //
    private String url;
    private boolean isRead;
    private boolean isDeleted;
    private LocalDateTime createdAt;

    public NotificationResponse(Notification notification, NotificationType notificationType, UserProfile userProfile) {
        this.id = notification.getId();
        this.title = notificationType.getTitle();
        this.userName = userProfile.getNickname();
        this.picUrl = userProfile.getPicUrl();
        this.message = notification.getMessage();
        this.url = notification.getUrl();
        this.isRead = notification.isRead();
        this.isDeleted = notification.isDeleted();
        this.createdAt = notification.getCreatedAt();
    }
}
