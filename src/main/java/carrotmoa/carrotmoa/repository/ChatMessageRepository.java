package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomId(long chatRoomId);
}
