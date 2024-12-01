package carrotmoa.carrotmoa.entity;

import carrotmoa.carrotmoa.enums.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "product")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "product_category_id")
    private Long productCategoryId;

    @Column(name = "price")
    private Integer price;

    @Column(name = "is_price_offer")
    private boolean isPriceOffer;

    @Column(name = "is_free")
    private boolean isFree;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;
}
