package carrotmoa.carrotmoa.model.response;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * carrotmoa.carrotmoa.model.response.QAccommodationDetailResponse is a Querydsl Projection type for AccommodationDetailResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QAccommodationDetailResponse extends ConstructorExpression<AccommodationDetailResponse> {

    private static final long serialVersionUID = 2074462880L;

    public QAccommodationDetailResponse(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<Integer> totalArea, com.querydsl.core.types.Expression<String> roadAddress, com.querydsl.core.types.Expression<String> lotAddress, com.querydsl.core.types.Expression<String> detailAddress, com.querydsl.core.types.Expression<Integer> floor, com.querydsl.core.types.Expression<Integer> totalFloor, com.querydsl.core.types.Expression<? extends java.math.BigDecimal> price, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<String> transportationInfo) {
        super(AccommodationDetailResponse.class, new Class<?>[]{long.class, String.class, int.class, String.class, String.class, String.class, int.class, int.class, java.math.BigDecimal.class, String.class, String.class}, id, title, totalArea, roadAddress, lotAddress, detailAddress, floor, totalFloor, price, content, transportationInfo);
    }

}

