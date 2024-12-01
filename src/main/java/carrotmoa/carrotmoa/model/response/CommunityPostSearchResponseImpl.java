package carrotmoa.carrotmoa.model.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CommunityPostSearchResponseImpl implements CommunityPostSearchResponse {
    private Long postId;
    private String addressName;
    private String content;
    private String postImageUrl;

    @Override
    public Long getPostId() {
        return postId;
    }

    @Override
    public String getAddressName() {
        return addressName;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getPostImageUrl() {
        return postImageUrl;
    }

    public String getPostUrl() {
        return "/community/posts/" + postId;
    }

}
