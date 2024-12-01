package carrotmoa.carrotmoa.model.response;

import carrotmoa.carrotmoa.entity.PostImage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FleaMarketPostImageResponse {

    private Long postId;
    private String imageUrl;

    public static FleaMarketPostImageResponse toFleaMarketPostImageResponse(PostImage postImage) {
        return FleaMarketPostImageResponse.builder()
            .postId(postImage.getPostId())
            .imageUrl(postImage.getImageUrl())
            .build();
    }
}
