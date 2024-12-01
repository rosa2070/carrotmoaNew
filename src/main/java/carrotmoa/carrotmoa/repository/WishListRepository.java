package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO wishlist (user_id, post_id, is_canceled, created_at, updated_at) " +
        "VALUES (:userId, :postId, 0, NOW(), NOW()) " +
        "ON DUPLICATE KEY UPDATE is_canceled = IF(is_canceled = 0, 1, 0), updated_at = NOW()", nativeQuery = true)
    void updateWishListByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
}
