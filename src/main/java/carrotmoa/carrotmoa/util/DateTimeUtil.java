package carrotmoa.carrotmoa.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    // 날짜 포맷을 yyyy-MM-dd 형식으로 정의
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 생성일자와 현재 시간의 차이를 계산해 경과 시간을 문자열로 반환하는 메서드
     *
     * @param createdAt 생성일자
     * @return 경과 시간 문자열
     */

    public static String formatElapsedTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        if (duration.getSeconds() < 60) {
            if (duration.getSeconds() == 0) {
                return "1초 전";
            }
            return duration.getSeconds() + "초 전";
        } else if (duration.toMinutes() < 60) {
            return duration.toMinutes() + "분 전";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + "시간 전";
        } else if (duration.toDays() < 30) {
            return duration.toDays() + "일 전";
        } else if (duration.toDays() < 365) {
            return duration.toDays() / 30 + "개월 전";
        } else {
            return createdAt.format(DATE_FORMATTER);  // 1년 이상 지난 경우 yyyy-MM-dd 형식으로 반환
        }
    }
}

