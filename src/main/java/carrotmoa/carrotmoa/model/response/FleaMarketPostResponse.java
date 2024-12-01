package carrotmoa.carrotmoa.model.response;

import carrotmoa.carrotmoa.enums.ProductStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FleaMarketPostResponse {

    private Long id;
    private Long userId;
    private String title;
    private String content;
    private Long productId;
    private Integer price;
    private boolean isPriceOffer;
    private boolean isFree;
    private ProductStatus productStatus;
    private String region1DepthName;
    private String region2DepthName;
    private String region3DepthName;
    private LocalDateTime createdAt;

    public FleaMarketPostResponse(
        Long id,
        Long userId,
        String postTitle,
        String content,
        Long productId,
        Integer price,
        boolean isPriceOffer,
        boolean isFree,
        ProductStatus productStatus,
        String region1DepthName,
        String region2DepthName,
        String region3DepthName,
        LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = postTitle;
        this.content = content;
        this.productId = productId;
        this.price = price;
        this.isPriceOffer = isPriceOffer;
        this.isFree = isFree;
        this.productStatus = productStatus;
        this.region1DepthName = region1DepthName;
        this.region2DepthName = region2DepthName;
        this.region3DepthName = region3DepthName;
        this.createdAt = createdAt;
    }
}
