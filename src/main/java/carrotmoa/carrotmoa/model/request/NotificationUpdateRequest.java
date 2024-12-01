package carrotmoa.carrotmoa.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NotificationUpdateRequest {
    private Long id;          // 알림 ID
    @JsonProperty("isRead")
    private boolean isRead;   // 읽음 여부
    @JsonProperty("isDeleted")
    private boolean isDeleted; // 삭제 여부
}
