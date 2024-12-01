package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.entity.PostLike;
import carrotmoa.carrotmoa.repository.CommunityPostRepository;
import carrotmoa.carrotmoa.repository.PostLikeRepository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private static final String LIKE_KEY_FORMAT = "post:%d:user:%d:like:isCanceled";
    private static final String LIKE_COUNT_KEY_FORMAT = "post:%d:likeCount";
    private final PostLikeRepository postLikeRepository;
    private final CommunityPostRepository communityPostRepository;
    private final RedisTemplate<String, Object> likeRedisTemplate;

    @Transactional
    public boolean toggleLike(Long postId, Long userId) {
        Long communityPostId = communityPostRepository.findPostIdById(postId);
        String redisKey = String.format(LIKE_KEY_FORMAT, communityPostId, userId);
        Boolean cachedLikeStatus = (Boolean) likeRedisTemplate.opsForValue().get(redisKey);

        boolean newStatus;
        // 1. Redis에 좋아요 상태가 존재할 경우
        if (cachedLikeStatus != null) {
            newStatus = !cachedLikeStatus;
        } else {
            // DB에 해당 사용자와 게시글의 좋아요 상태를 조회
            Optional<PostLike> existingLike = postLikeRepository.findByPostIdAndUserId(communityPostId, userId);

            if (existingLike.isEmpty()) {
                // 새로 좋아요 추가
                newStatus = false;
                postLikeRepository.save(new PostLike(communityPostId, userId, "LIKE", newStatus));
            } else {
                // 기존 좋아요 상태 반전
                newStatus = !existingLike.get().isCanceled();
                existingLike.get().setCanceled(newStatus);
                postLikeRepository.save(existingLike.get());
            }
        }

        // Redis에 새로운 상태를 저장하고 만료 시간 설정
        likeRedisTemplate.opsForValue().set(redisKey, newStatus, 1, TimeUnit.DAYS);
        updateDatabaseLikeStatus(communityPostId, userId, newStatus);
        // 좋아요 개수 업데이트
        updateLikeCount(communityPostId, newStatus);
        return !newStatus; // UI에서는 좋아요 상태가 false일 때 활성화되므로 반전된 값을 반환
    }

    private void updateDatabaseLikeStatus(Long postId, Long userId, boolean isCanceled) {
        Optional<PostLike> existingLike = postLikeRepository.findByPostIdAndUserId(postId, userId);
        existingLike.ifPresent(postLike -> {
            postLike.setCanceled(isCanceled);
            postLikeRepository.save(postLike);
        });
    }

    // 좋아요 개수 업데이트 메서드 추가
    private void updateLikeCount(Long postId, boolean isCanceled) {
        String likeCountKey = String.format(LIKE_COUNT_KEY_FORMAT, postId);

        if (!isCanceled) {
            // 좋아요 추가 시 +1
            likeRedisTemplate.opsForValue().increment(likeCountKey);
        } else {
            // 좋아요 취소 시 -1
            likeRedisTemplate.opsForValue().decrement(likeCountKey);
        }
        likeRedisTemplate.expire(likeCountKey, 1, TimeUnit.DAYS);
    }

    // 특정 게시글의 좋아요 개수 조회 메서드
    public int getLikeCount(Long postId) {
        Long communityPostId = communityPostRepository.findPostIdById(postId);
        String likeCountKey = String.format(LIKE_COUNT_KEY_FORMAT, communityPostId);
        Integer likeCount = (Integer) likeRedisTemplate.opsForValue().get(likeCountKey);

        if (likeCount == null) {
            // Redis에 없으면 DB에서 조회
            likeCount = postLikeRepository.countByPostIdAndIsCanceledFalse(communityPostId);
            likeRedisTemplate.opsForValue().set(likeCountKey, likeCount, 1, TimeUnit.DAYS);
        }
        return likeCount;
    }

    public Boolean findIsCanceledByPostIdAndUserId(Long postId, Long userId) {
        Long communityPostId = communityPostRepository.findPostIdById(postId);
        // 좋아요를 한 번도 안누른 유저면 true, 해당 게시글에 좋아요 누른 유저면 fasle, 취소한 유저면 (좋아요 안누른 유저) true 반환
        // 즉, 좋아요 안누른 유저면 true, 누른 유저면 fasle 반환.
        return postLikeRepository.findIsCanceledByPostIdAndUserId(communityPostId, userId).orElse(true);
    }
}