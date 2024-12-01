package carrotmoa.carrotmoa.model.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * carrotmoa.carrotmoa.model.response.QCommunityCommentResponse is a Querydsl Projection type for CommunityCommentResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCommunityCommentResponse extends ConstructorExpression<CommunityCommentResponse> {

    private static final long serialVersionUID = 1448315931L;

    public QCommunityCommentResponse(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<Long> communityPostId, com.querydsl.core.types.Expression<Long> parentId, com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<String> picUrl, com.querydsl.core.types.Expression<String> region2DepthName, com.querydsl.core.types.Expression<String> region3DepthName, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Integer> depth, com.querydsl.core.types.Expression<Integer> orderInGroup, com.querydsl.core.types.Expression<Boolean> isDeleted, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> updatedAt) {
        super(CommunityCommentResponse.class, new Class<?>[]{long.class, long.class, long.class, long.class, String.class, String.class, String.class, String.class, String.class, int.class, int.class, boolean.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, id, communityPostId, parentId, userId, nickname, picUrl, region2DepthName, region3DepthName, content, depth, orderInGroup, isDeleted, createdAt, updatedAt);
    }

}

