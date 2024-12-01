package carrotmoa.carrotmoa.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FleaMarketPostUserResponse {

    private String picUrl;
    private String nickname;
    private String region1DepthName;
    private String region2DepthName;
    private String region3DepthName;

    public FleaMarketPostUserResponse(
        String picUrl,
        String nickname,
        String region1DepthName,
        String region2DepthName,
        String region3DepthName) {
        this.picUrl = picUrl;
        this.nickname = nickname;
        this.region1DepthName = region1DepthName;
        this.region2DepthName = region2DepthName;
        this.region3DepthName = region3DepthName;
    }
}
