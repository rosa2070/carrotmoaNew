package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.ChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllById(long roomId);
}
