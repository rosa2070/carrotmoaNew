package carrotmoa.carrotmoa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "community_comment")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityComment extends BaseEntity {

    @Column(name = "community_post_id")
    private Long communityPostId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "content")
    private String content;

    @Column(name = "depth")
    private int depth;

    @Column(name = "order_in_group")
    private int orderInGroup;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public void softDeleteComment(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public CommunityComment createReply(Long communityPostId, Long userId, String content) {
        CommunityComment replyComment = new CommunityComment();
        replyComment.setCommunityPostId(communityPostId);
        replyComment.setParentId(this.getId()); // 현재 댓글의 ID를 부모 ID로 설정
        replyComment.setUserId(userId);
        replyComment.setContent(content);
        replyComment.setDepth(this.depth + 1); // 부모 댓글의 depth + 1
        return replyComment;
    }
}
