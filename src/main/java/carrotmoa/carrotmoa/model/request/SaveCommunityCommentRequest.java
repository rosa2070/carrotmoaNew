package carrotmoa.carrotmoa.model.request;

import carrotmoa.carrotmoa.entity.CommunityComment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SaveCommunityCommentRequest {
    private Long id;
    private Long communityPostId;
    private Long parentId;
    private Long userId;
    private String content;
    private int depth;
    private int orderInGroup;
    private boolean isDeleted;


    public CommunityComment toCommunityCommentEntity() {
        return CommunityComment.builder()
            .communityPostId(communityPostId)
            .parentId(parentId)
            .userId(userId)
            .content(content)
            .depth(depth)
            .orderInGroup(orderInGroup)
            .isDeleted(false)
            .build();
    }


}
