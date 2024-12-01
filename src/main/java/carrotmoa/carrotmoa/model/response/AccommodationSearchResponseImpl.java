package carrotmoa.carrotmoa.model.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AccommodationSearchResponseImpl implements AccommodationSearchResponse {
    private Long accommodationId;
    private String title;
    private String roadAddress;
    private BigDecimal price;
    private String imageUrl;
    private Long roomCount;
    private Long bathRoomCount;
    private Long livingRoomCount;
    private Long kitchenCount;

    @Override
    public Long getAccommodationId() {
        return accommodationId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getRoadAddress() {
        return roadAddress;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public Long getRoomCount() {
        return roomCount;
    }

    @Override
    public Long getBathRoomCount() {
        return bathRoomCount;
    }

    @Override
    public Long getLivingRoomCount() {
        return livingRoomCount;
    }

    @Override
    public Long getKitchenCount() {
        return kitchenCount;
    }

    public String getAccommodationUrl() {
        return "/room/detail/" + accommodationId;
    }
}
