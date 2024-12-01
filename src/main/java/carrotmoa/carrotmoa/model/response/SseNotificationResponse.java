package carrotmoa.carrotmoa.model.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SseNotificationResponse {
    private Long id;
    private String title;
    private String userName;
    private String picUrl;
    private String message;
    private String url;
    private boolean isRead;
    private boolean isDeleted;
    private LocalDateTime createdAt;
}
