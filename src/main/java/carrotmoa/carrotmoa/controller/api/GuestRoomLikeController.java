package carrotmoa.carrotmoa.controller.api;

import carrotmoa.carrotmoa.service.AccommodationLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room")
public class GuestRoomLikeController {

    private final AccommodationLikeService accommodationLikeService;

    public GuestRoomLikeController(AccommodationLikeService accommodationLikeService) {
        this.accommodationLikeService = accommodationLikeService;
    }

    // 숙소 찜하기 기능 처리
    @PostMapping("/{id}/like")
    public ResponseEntity<String> toggleLike(@PathVariable("id") Long accommodationId,
                                             @RequestParam("userId") Long userId) {

        // 찜하기 여부 확인
        ResponseEntity<Boolean> isLikedResponse = accommodationLikeService.isLiked(userId, accommodationId);
        Boolean isLiked = isLikedResponse.getBody();

        // 찜하기 추가 또는 취소
        if (isLiked != null && isLiked) {
            // 이미 찜한 상태라면 찜 취소
            return accommodationLikeService.removeLike(userId, accommodationId);
        } else {
            // 찜하지 않았다면 찜하기 추가
            return accommodationLikeService.addLike(userId, accommodationId);
        }

    }

    // 사용자가 찜한 숙소인지 확인 (조회)
    @GetMapping("/{id}/like")
    public ResponseEntity<Boolean> checkLikeStatus(@PathVariable("id") Long accommodationId,
                                                   @RequestParam("userId") Long userId) {
        return accommodationLikeService.isLiked(userId, accommodationId);
    }

}
