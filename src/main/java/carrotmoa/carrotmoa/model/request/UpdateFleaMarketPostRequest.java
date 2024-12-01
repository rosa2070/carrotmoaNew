package carrotmoa.carrotmoa.model.request;

import carrotmoa.carrotmoa.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFleaMarketPostRequest {

    private Long serviceId;
    private Long userId;
    private String title;
    private String content;
    private boolean isDeleted;
    private Long postId;
    private Long productCategoryId;
    private Integer price;
    private boolean isPriceOffer;
    private boolean isFree;
    private ProductStatus status;
}
