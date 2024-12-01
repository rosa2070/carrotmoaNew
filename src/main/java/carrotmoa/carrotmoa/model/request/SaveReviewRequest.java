package carrotmoa.carrotmoa.model.request;

import carrotmoa.carrotmoa.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaveReviewRequest {
    private Long postId;
    private Long userId;
    private String comment;

    public Review toEntity() {
        return Review.builder()
            .postId(postId)
            .userId(userId)
            .comment(comment)
            .build();
    }
}
