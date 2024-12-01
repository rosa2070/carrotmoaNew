package carrotmoa.carrotmoa.model.request;

import carrotmoa.carrotmoa.entity.Post;
import carrotmoa.carrotmoa.entity.Product;
import carrotmoa.carrotmoa.enums.ProductStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveFleaMarketPostRequest {

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

    public Post toPostEntity() {
        return Post.builder()
            .serviceId(1L)
            .userId(userId)
            .title(title)
            .content(content)
            .isDeleted(false)
            .build();
    }

    public Product toProductEntity(Long id) {
        return Product.builder()
            .postId(id)
            .productCategoryId(productCategoryId)
            .price(price)
            .isPriceOffer(isPriceOffer)
            .isFree(isFree)
            .status(ProductStatus.ON_SALE)
            .build();
    }


}
