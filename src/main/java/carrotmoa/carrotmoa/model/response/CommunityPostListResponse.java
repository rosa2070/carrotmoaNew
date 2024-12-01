package carrotmoa.carrotmoa.model.response;

import carrotmoa.carrotmoa.entity.CommunityCategory;
import carrotmoa.carrotmoa.entity.CommunityPost;
import carrotmoa.carrotmoa.entity.Post;
import carrotmoa.carrotmoa.entity.PostImage;
import carrotmoa.carrotmoa.entity.UserAddress;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CommunityPostListResponse {

    private Long communityPostId;
    private String categoryName;
    private String region3DepthName;
    private String title;
    private String content;
    private String imageUrl;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String formattedCreatedAt;
    private String formattedUpdatedAt;


    public CommunityPostListResponse(Post post, CommunityPost communityPost, CommunityCategory communityCategory, UserAddress userAddress,
        PostImage postImage, int commentCount) {
        this.communityPostId = communityPost.getId();
        this.categoryName = communityCategory.getName();
        this.region3DepthName = userAddress.getRegion3DepthName();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrl = (postImage != null) ? postImage.getImageUrl() : null;
        this.commentCount = commentCount;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
