package carrotmoa.carrotmoa.repository;

import carrotmoa.carrotmoa.entity.QCommunityComment;
import carrotmoa.carrotmoa.entity.QUserAddress;
import carrotmoa.carrotmoa.entity.QUserProfile;
import carrotmoa.carrotmoa.model.response.CommunityCommentResponse;
import carrotmoa.carrotmoa.model.response.QCommunityCommentResponse;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommunityCommentCustomRepositoryImpl implements CommunityCommentCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommunityCommentResponse> findCommentsByPostIdOrdered(Long communityPostId) {
        QCommunityComment communityComment = QCommunityComment.communityComment;
        QCommunityComment replyComment = new QCommunityComment("replyComment"); // 답글 alias
        QUserProfile userProfile = QUserProfile.userProfile;
        QUserAddress userAddress = QUserAddress.userAddress;

        return queryFactory
            .select(new QCommunityCommentResponse(
                communityComment.id,
                communityComment.communityPostId,
                communityComment.parentId,
                communityComment.userId,
                userProfile.nickname,
                userProfile.picUrl,
                userAddress.region2DepthName,
                userAddress.region3DepthName,
                communityComment.content,
                communityComment.depth,
                communityComment.orderInGroup,
                communityComment.isDeleted,
                communityComment.createdAt,
                communityComment.updatedAt
            ))
            .from(communityComment)
            .join(userProfile).on(communityComment.userId.eq(userProfile.userId))
            .join(userAddress).on(communityComment.userId.eq(userAddress.userId))
            .where(
                communityComment.communityPostId.eq(communityPostId)
                    .and(
                        communityComment.isDeleted.eq(false)
                            .or(
                                communityComment.isDeleted.eq(true)
                                    .and(
                                        // 답글이 하나라도 있는지 확인
                                        JPAExpressions.selectOne()
                                            .from(replyComment)
                                            .where(replyComment.parentId.eq(communityComment.id)
                                                .and(replyComment.isDeleted.eq(false)))
                                            .exists()
                                    )
                            )
                    )
            )
            .orderBy(communityComment.orderInGroup.asc(), communityComment.depth.asc())
            .fetch();
    }

}