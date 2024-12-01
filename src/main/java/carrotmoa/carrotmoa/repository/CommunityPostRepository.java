package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.CommunityPost;
import carrotmoa.carrotmoa.model.response.CommunityPostDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    // TODO: JPQL 수정 예정, 추후에 유저 온도 받아오기
    @Query(
        "SELECT new carrotmoa.carrotmoa.model.response.CommunityPostDetailResponse(cp.id, cp.postId, cp.communityCategoryId, p.userId, p.title, p.content, p.isDeleted, cat.name, up.nickname, up.picUrl, ua.region2DepthName, ua.region3DepthName, p.createdAt, p.updatedAt) "
            +
            "FROM CommunityPost cp " +
            "JOIN Post p ON cp.postId = p.id " +
            "JOIN CommunityCategory cat ON cp.communityCategoryId = cat.id " +
            "JOIN UserProfile up ON p.userId = up.userId " + // UserProfile 조인
            "JOIN UserAddress ua ON p.userId = ua.userId " + // UserAddress 조인
            "WHERE cp.id = :id AND p.isDeleted = false")
    CommunityPostDetailResponse findCommunityPostDetail(@Param("id") Long id);

    @Query("SELECT cp.postId FROM CommunityPost cp WHERE cp.id = :id")
    Long findPostIdById(@Param("id") Long id);

}

