package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.PostLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

    int countByPostIdAndIsCanceledFalse(Long postId);


    @Query("select pl.isCanceled from PostLike pl where pl.postId = :postId and pl.userId = :userId")
    Optional<Boolean> findIsCanceledByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
