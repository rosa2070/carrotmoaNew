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
public class SaveCommunityPostRequest {

    private Long id;
    private Long serviceId;
    private Long userId;
    private String title;
    private String content;
    private boolean isDeleted;
    private Long communityPostId;
    private Long postId;
    private Long communityCategoryId;

    public SaveCommunityPostRequest(Post post) {
        this.id = post.getId();
        this.serviceId = post.getServiceId();
        this.userId = post.getUserId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.isDeleted = post.isDeleted();
    }

    public SaveCommunityPostRequest(CommunityPost communityPost) {
        this.communityPostId = communityPost.getId();
        this.postId = communityPost.getPostId();
        this.communityCategoryId = communityPost.getCommunityCategoryId();
    }

    public Post toPostEntity() {
        return Post.builder()
            .serviceId(serviceId)
            .userId(userId)
            .title(title)
            .content(content)
            .isDeleted(false)
            .build();
    }

    public CommunityPost toCommunityPostEntity(Long id) {
        return CommunityPost.builder()
            .postId(id)
            .communityCategoryId(communityCategoryId)
            .build();
    }
}
