package carrotmoa.carrotmoa.model.response;

import carrotmoa.carrotmoa.entity.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductCategoryResponse {

    private Long id;
    private String name;

    public static ProductCategoryResponse toProductCategoryResponse(ProductCategory productCategory) {
        return ProductCategoryResponse.builder()
            .id(productCategory.getId())
            .name(productCategory.getName())
            .build();
    }
}
