package carrotmoa.carrotmoa.model.response;

import carrotmoa.carrotmoa.enums.ProductStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FleaMarketPostDetailResponse {

    private Long id;
    private Long userId;
    private String productCategoryName;
    private String picUrl;
    private String nickname;
    private String region1DepthName;
    private String region2DepthName;
    private String region3DepthName;
    private String title;
    private String content;
    private Integer price;
    private boolean isPriceOffer;
    private boolean isFree;
    private ProductStatus productStatus;
    private LocalDateTime createdAt;

    public static FleaMarketPostDetailResponse toFleaMarketPostDetailResponse(FleaMarketPostProductResponse fleaMarketPostProductResponse,
        FleaMarketPostUserResponse fleaMarketPostUserResponse) {

        String picUrl = (fleaMarketPostUserResponse != null) ? fleaMarketPostUserResponse.getPicUrl() : "기본 이미지 URL";
        String nickname = (fleaMarketPostUserResponse != null) ? fleaMarketPostUserResponse.getNickname() : "알 수 없는 사용자";

        return builder()
            .id(fleaMarketPostProductResponse.getId())
            .userId(fleaMarketPostProductResponse.getUserId())
            .productCategoryName(fleaMarketPostProductResponse.getProductCategoryName())
            .picUrl(picUrl)
            .nickname(nickname)
            .region1DepthName((fleaMarketPostUserResponse != null) ? fleaMarketPostUserResponse.getRegion1DepthName() : "기본 지역1")
            .region2DepthName((fleaMarketPostUserResponse != null) ? fleaMarketPostUserResponse.getRegion2DepthName() : "기본 지역2")
            .region3DepthName((fleaMarketPostUserResponse != null) ? fleaMarketPostUserResponse.getRegion3DepthName() : "기본 지역3")
            .title(fleaMarketPostProductResponse.getTitle())
            .content(fleaMarketPostProductResponse.getContent())
            .price(fleaMarketPostProductResponse.getPrice())
            .isPriceOffer(fleaMarketPostProductResponse.isPriceOffer())
            .isFree(fleaMarketPostProductResponse.isFree())
            .productStatus(fleaMarketPostProductResponse.getProductStatus())
            .createdAt(fleaMarketPostProductResponse.getCreatedAt())
            .build();
    }
}
