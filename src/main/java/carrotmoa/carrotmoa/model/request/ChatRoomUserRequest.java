package carrotmoa.carrotmoa.model.request;

import carrotmoa.carrotmoa.entity.ChatRoomUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomUserRequest {
    private long id;
    private long userId;
    private long chatRoomId;
    private int state;

    public ChatRoomUserRequest(long userId, long chatRoomId) {
        this.userId = userId;
        this.chatRoomId = chatRoomId;
        this.state = 0;
    }

    public ChatRoomUser toEntityChatRoomUser() {
        return ChatRoomUser.builder()
            .userId(this.userId)
            .chatRoomId(this.chatRoomId)
            .state(this.state)
            .build();
    }

}
