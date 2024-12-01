package carrotmoa.carrotmoa.model.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommunityCommentResponse {
    private Long id;
    private Long communityPostId;
    private Long parentId;
    private Long userId;
    private String nickname;
    private String picUrl;
    private String region2DepthName;
    private String region3DepthName;
    private String content;
    private int depth;
    private int orderInGroup;
    private boolean isDeleted;
    // 추가된 필드
    private List<CommunityCommentResponse> replies = new ArrayList<>(); // 답글을 저장할 리스트
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String formattedCreatedAt;
    private String formattedUpdatedAt;

//    public CommunityCommentResponse(CommunityComment comment, UserProfile userProfile, UserAddress userAddress) {
//        this.id = comment.getId();
//        this.communityPostId = comment.getCommunityPostId();
//        this.parentId = comment.getParentId();
//        this.userId = comment.getUserId();
//        this.nickname = userProfile.getNickname();
//        this.picUrl = userProfile.getPicUrl();
//        this.region2DepthName = userAddress.getRegion2DepthName();
//        this.region3DepthName = userAddress.getRegion3DepthName();
//        this.content = comment.getContent();
//        this.depth = comment.getDepth();
//        this.orderInGroup = comment.getOrderInGroup();
//        this.isDeleted = comment.isDeleted();
//        this.createdAt = comment.getCreatedAt();
//        this.updatedAt = comment.getUpdatedAt();
//    }

    @QueryProjection
    public CommunityCommentResponse(Long id, Long communityPostId, Long parentId, Long userId,
        String nickname, String picUrl, String region2DepthName,
        String region3DepthName, String content, int depth, int orderInGroup,
        boolean isDeleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.communityPostId = communityPostId;
        this.parentId = parentId;
        this.userId = userId;
        this.nickname = nickname;
        this.picUrl = picUrl;
        this.region2DepthName = region2DepthName;
        this.region3DepthName = region3DepthName;
        this.content = content;
        this.depth = depth;
        this.orderInGroup = orderInGroup;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // addReply 메서드 추가
    public void addReply(CommunityCommentResponse reply) {
        replies.add(reply); // replies 리스트에 답글 추가
    }


}
