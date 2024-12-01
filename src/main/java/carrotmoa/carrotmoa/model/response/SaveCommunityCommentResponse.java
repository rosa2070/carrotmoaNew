package carrotmoa.carrotmoa.model.response;

import carrotmoa.carrotmoa.entity.CommunityComment;
import carrotmoa.carrotmoa.entity.UserAddress;
import carrotmoa.carrotmoa.entity.UserProfile;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SaveCommunityCommentResponse {
    // 유저 프로필 테이블
    private String nickname;

    // 유저 동네생활 주소 테이블
    private String region2DepthName;
    private String region3DepthName;

    // 댓글 테이블
    private Long id;
    private Long userId;
    private Long communityPostId;
    private Long parentId;
    private String content;
    private boolean isDeleted;

    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SaveCommunityCommentResponse(CommunityComment comment, UserProfile userProfile, UserAddress userAddress, int commentCount) {
        this.id = comment.getId();
        this.userId = comment.getUserId();
        this.communityPostId = comment.getCommunityPostId();
        this.parentId = comment.getParentId();
        this.content = comment.getContent();
        this.isDeleted = comment.isDeleted();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.nickname = userProfile.getNickname();
        this.region2DepthName = userAddress.getRegion2DepthName();
        this.region3DepthName = userAddress.getRegion3DepthName();
        this.commentCount = commentCount;
    }


}
