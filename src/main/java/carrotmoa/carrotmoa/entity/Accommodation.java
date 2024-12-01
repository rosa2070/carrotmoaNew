package carrotmoa.carrotmoa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "accommodation")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Accommodation extends BaseEntity {

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "total_area")
    private Integer totalArea;

    @Column(name = "road_address")
    private String roadAddress;

    @Column(name = "lot_address")
    private String lotAddress;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "total_floor")
    private Integer totalFloor;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "transportation_info")
    private String transportationInfo;

    public void updateAccommodation(Integer totalArea, String roadAddress, String lotAddress,
        String detailAddress, Integer floor, Integer totalFloor,
        BigDecimal price, String transportationInfo) {
        if (totalArea != null) {
            this.totalArea = totalArea;
        }
        if (roadAddress != null) {
            this.roadAddress = roadAddress;
        }
        if (lotAddress != null) {
            this.lotAddress = lotAddress;
        }
        if (detailAddress != null) {
            this.detailAddress = detailAddress;
        }
        if (floor != null) {
            this.floor = floor;
        }
        if (totalFloor != null) {
            this.totalFloor = totalFloor;
        }
        if (price != null) {
            this.price = price;
        }
        if (transportationInfo != null) {
            this.transportationInfo = transportationInfo;
        }
    }
}