package carrotmoa.carrotmoa.model.response;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccommodationDetailResponse {

    private Long id;
    private String title;
    private int totalArea;
    private String roadAddress;
    private String lotAddress;
    private String detailAddress;
    private Integer floor;
    private Integer totalFloor;
    private BigDecimal price;
    private String content;
    private String transportationInfo;
    private List<String> imageUrls;
    private List<Long> amenityIds;
    private List<Integer> spaceCounts;

    @QueryProjection
    public AccommodationDetailResponse(Long id, String title, int totalArea, String roadAddress, String lotAddress, String detailAddress,
        Integer floor, Integer totalFloor, BigDecimal price, String content, String transportationInfo) {
        this.id = id;
        this.title = title;
        this.totalArea = totalArea;
        this.roadAddress = roadAddress;
        this.lotAddress = lotAddress;
        this.detailAddress = detailAddress;
        this.floor = floor;
        this.totalFloor = totalFloor;
        this.price = price;
        this.content = content;
        this.transportationInfo = transportationInfo;
    }
}



