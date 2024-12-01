package carrotmoa.carrotmoa.model.request;

import carrotmoa.carrotmoa.config.security.CustomUserDetails;
import carrotmoa.carrotmoa.entity.ChatMessage;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequest {
    private long userId;
    private long chatRoomId;
    private String message;
    private int state;
    private LocalDateTime createdAt;

    public ChatMessageRequest(ChatMessage chatMessage) {
        this.userId = chatMessage.getUserId();
        this.chatRoomId = chatMessage.getChatRoomId();
        this.message = chatMessage.getMessage();
        this.state = chatMessage.getState();
        this.createdAt = chatMessage.getCreatedAt();
    }

    public ChatMessageRequest(CustomUserDetails user) {
        this.userId = user.getUserProfile().getUserId();
    }

    public ChatMessage toEntityChatMessage() {
        return ChatMessage.builder()
            .userId(this.getUserId())
            .chatRoomId(this.getChatRoomId())
            .message(this.getMessage())
            .state(this.getState())
            .build();
    }

}

