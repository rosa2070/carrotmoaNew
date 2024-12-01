package carrotmoa.carrotmoa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    LIKE(1L, "게시물에 좋아요가 눌렸습니다."),
    COMMENT(2L, "내 게시물에 댓글이 달렸습니다."),
    REPLY(3L, "내 댓글에 답글이 달렸습니다."),
    WISHLIST(4L, "내 게시물에 찜을 했습니다."),
    BOOKING(5L, "숙소가 예약되었습니다."),
    GUEST_WISHLIST(6L, "숙소에 찜을 했습니다."),
    RESERVATION_CONFIRM(7L, "새 계약이 확인되었습니다."),
    GUEST_CANCEL(8L, "결제가 취소되었습니다.");

    private final Long typeId;
    private final String title;
}