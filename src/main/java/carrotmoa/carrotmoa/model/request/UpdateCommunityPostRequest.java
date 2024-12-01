package carrotmoa.carrotmoa.model.request;

import carrotmoa.carrotmoa.entity.CommunityPost;
import carrotmoa.carrotmoa.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UpdateCommunityPostRequest {
    private String title;
    private String content;
    private Long communityCategoryId;

    public UpdateCommunityPostRequest(Post post, CommunityPost communityPost) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.communityCategoryId = communityPost.getCommunityCategoryId();
    }

    public Post toPostEntity() {
        return Post.builder()
            .title(title)
            .content(content)
            .build();
    }

    public CommunityPost toCommunityPostEntity() {
        return CommunityPost.builder()
            .communityCategoryId(communityCategoryId)
            .build();
    }
}
