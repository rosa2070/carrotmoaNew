package carrotmoa.carrotmoa.model.response;

import carrotmoa.carrotmoa.enums.ProductStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FleaMarketPostProductResponse {

    private Long id;
    private Long userId;
    private String productCategoryName;
    private String title;
    private String content;
    private Integer price;
    private boolean isPriceOffer;
    private boolean isFree;
    private ProductStatus productStatus;
    private LocalDateTime createdAt;

    public FleaMarketPostProductResponse(
        Long id,
        Long userId,
        String productCategoryName,
        String title,
        String content,
        Integer price,
        boolean isPriceOffer,
        boolean isFree,
        ProductStatus productStatus,
        LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.productCategoryName = productCategoryName;
        this.title = title;
        this.content = content;
        this.price = price;
        this.isPriceOffer = isPriceOffer;
        this.isFree = isFree;
        this.productStatus = productStatus;
        this.createdAt = createdAt;
    }
}
