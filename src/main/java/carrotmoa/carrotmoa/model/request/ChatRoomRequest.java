package carrotmoa.carrotmoa.model.request;

import carrotmoa.carrotmoa.entity.ChatRoom;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequest {
    private long roomId;
    private String roomName;
    private int state;
    private LocalDateTime createdAt;


    public ChatRoomRequest(ChatRoom chatRoom, String findRelativeUserNickname) {
        this.roomId = chatRoom.getId();
        this.state = chatRoom.getState();
        if (chatRoom.getRoomName() != null) {
            this.roomName = chatRoom.getRoomName();
        } else {
            this.roomName = findRelativeUserNickname;
        }
        this.createdAt = chatRoom.getCreatedAt();
    }

    public ChatRoom toEntityChatRoom() {
        return ChatRoom.builder()
            .roomName(this.roomName)
            .state(0)
            .build();
    }
}
