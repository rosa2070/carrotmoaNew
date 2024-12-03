package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.AccommodationLike;
import carrotmoa.carrotmoa.repository.AccommodationLikeRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class AccommodationLikeService {

    private final RedisTemplate<String, Object> roomLikeRedisTemplate;
    private final AccommodationLikeRepository accommodationLikeRepository;

    public AccommodationLikeService(RedisTemplate<String, Object> roomLikeRedisTemplate,
                                    AccommodationLikeRepository accommodationLikeRepository) {
        this.roomLikeRedisTemplate = roomLikeRedisTemplate;
        this.accommodationLikeRepository = accommodationLikeRepository;
    }

    private static final String LIKE_KEY_PREFIX = "accommodationLike:";

    // 찜하기 추가
    @Transactional
    public ResponseEntity<String> addLike(Long userId, Long accommodationId) {
        String redisKey = LIKE_KEY_PREFIX + userId + ":" + accommodationId;

        // Redis에 찜하기 저장 (TTL 설정 예: 7일)
        roomLikeRedisTemplate.opsForValue().set(redisKey, Boolean.TRUE,7, TimeUnit.DAYS);

        // RDB에도 저장
        AccommodationLike accommodationLike = AccommodationLike.builder()
                .userId(userId)
                .accommodationId(accommodationId)
                .build();
        accommodationLikeRepository.save(accommodationLike);

        return ResponseEntity.ok("찜하기 성공");
    }

    // 찜하기 취소
    @Transactional
    public ResponseEntity<String> removeLike(Long userId, Long accommodationId) {
        String redisKey = LIKE_KEY_PREFIX + userId + ":" + accommodationId;

        // Redis에서 제거
        roomLikeRedisTemplate.delete(redisKey);

        // RDB에서 제거
        accommodationLikeRepository.deleteByUserIdAndAccommodationId(userId, accommodationId);

        return ResponseEntity.ok("찜하기 취소됨");
    }

    // 사용자가 찜한 숙소인지 확인
    // read-Through 전략
    public ResponseEntity<Boolean> isLiked(Long userId, Long accommodationId) {
        String redisKey = LIKE_KEY_PREFIX + userId + ":" + accommodationId;

        // Redis에서 찜 여부 확인
        Boolean isLiked = roomLikeRedisTemplate.hasKey(redisKey);

        if (!isLiked) {
            // Redis에 정보가 없으면, RDB에서 확인 후 Redis에 다시 저장
            // 캐시 미스가 발생한 경우, 즉 Redis에 정보가 없으면 RDB에서 직접 조회
            isLiked = accommodationLikeRepository.existsByUserIdAndAccommodationId(userId, accommodationId);

            if (isLiked) {
                // RDB에서 찜한 정보가 있다면, Redis에 다시 저장하여 다음 조회 시 Redis에서 바로 가져올 수 있도록 함
                roomLikeRedisTemplate.opsForValue().set(redisKey, " true", 7, TimeUnit.DAYS);
            }
        }

        return ResponseEntity.ok(isLiked);
    }



}
