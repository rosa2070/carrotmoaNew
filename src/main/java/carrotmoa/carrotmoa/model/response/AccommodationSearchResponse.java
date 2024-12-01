package carrotmoa.carrotmoa.model.response;

import java.math.BigDecimal;

public interface AccommodationSearchResponse {
    Long getAccommodationId();

    String getTitle();

    String getRoadAddress();

    BigDecimal getPrice();

    String getImageUrl();

    Long getRoomCount();

    Long getBathRoomCount();

    Long getLivingRoomCount();

    Long getKitchenCount();
}
