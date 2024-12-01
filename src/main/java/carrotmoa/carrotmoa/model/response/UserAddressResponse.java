package carrotmoa.carrotmoa.model.response;

import carrotmoa.carrotmoa.entity.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressResponse {
    private long addressId;
    private String addressName;
    private String code;
    private String region1DepthName;
    private String region2DepthName;
    private String region3DepthName;
    private String region4DepthName;
    private Double x;
    private Double y;

    public UserAddressResponse(UserAddress userAddress) {
        this.addressId = userAddress.getId();
        this.addressName = userAddress.getAddressName();
        this.code = userAddress.getCode();
        this.region1DepthName = userAddress.getRegion1DepthName();
        this.region2DepthName = userAddress.getRegion2DepthName();
        this.region3DepthName = userAddress.getRegion3DepthName();
        this.region4DepthName = userAddress.getRegion4DepthName();
        this.x = userAddress.getX();
        this.y = userAddress.getY();
    }
}
