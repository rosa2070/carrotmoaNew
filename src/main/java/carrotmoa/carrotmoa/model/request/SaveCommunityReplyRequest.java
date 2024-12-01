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
public class SaveCommunityReplyRequest {
    private Long userId;
    private String content;

    public CommunityComment toCommunityCommentEntity() {
        return CommunityComment.builder()
            .userId(userId)
            .content(content)
            .build();
    }
}
