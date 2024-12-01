package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.Notification;
import carrotmoa.carrotmoa.model.response.NotificationResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 1. 댓글 작성 시, 알림 테이블에 저장 -> 게시글 작성자에게 알림 전송

    // 2. 답글 작성 시, 알림 테이블에 저장 -> 댓글 작성자에게 알림 전송

    // 3. 게시글, 댓글에 좋아요 누를 경우 -> 알림 테이블에 저장 -> 해당 게시글, 댓글 작성자에게 좋아요 알림 전송

    // 4. 중고거래 찜 누를 경우 -> 알림 테이블에 저장 -> 해당 중고거래 게시글 작성자에게 알림 전송

    // 5. 숙소 알림 사용하는 경우 -> 여기에 추가하세요 !
    // 게스트 숙소 찜 알림

    // 6. 그 외에 알림을 사용할 경우 -> 여기에 메서드 생성~!

    // 7. 로그인한 유저(receiverId)의 isDeleted가 false인 모든 알림을 화면에 보여주기
    @Query("SELECT new carrotmoa.carrotmoa.model.response.NotificationResponse(" +
        "n, nt, up) " +
        "FROM Notification n " +
        "JOIN NotificationType nt ON n.typeId = nt.id " +
        "JOIN UserProfile up ON n.senderId = up.userId " +
        "WHERE n.receiverId = :receiverId AND n.isDeleted = false " +
        "ORDER BY n.createdAt DESC")
    List<NotificationResponse> findNotificationsByReceiverId(@Param("receiverId") Long receiverId);


}
