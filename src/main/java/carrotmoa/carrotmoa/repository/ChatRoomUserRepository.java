package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.ChatRoomUser;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    List<ChatRoomUser> findByUserId(long userId);

    @Query("SELECT a.userId FROM ChatRoomUser a WHERE a.userId != :userId AND a.chatRoomId = :chatRoomId")
    Long findRelativeUserId(@Param("userId") long userId, @Param("chatRoomId") long chatRoomId);

    @Query("SELECT a  FROM ChatRoomUser a WHERE a.chatRoomId IN (SELECT b.chatRoomId FROM ChatRoomUser b WHERE b.userId = :userId) AND a.userId = :joinUserId")
    Optional<ChatRoomUser> duplicateChatRoomId(@Param("userId") long userId, @Param("joinUserId") long joinUserId);
}
