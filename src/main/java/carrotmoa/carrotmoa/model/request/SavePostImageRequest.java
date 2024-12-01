package carrotmoa.carrotmoa.model.request;

import carrotmoa.carrotmoa.entity.PostImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class SavePostImageRequest {
    private Long postId;
    private String imageUrl;

    public PostImage toPostImageEntity() {
        return PostImage.builder()
            .postId(postId)
            .imageUrl(imageUrl)
            .build();
    }
}
