package carrotmoa.carrotmoa.model.request;

import carrotmoa.carrotmoa.entity.UserAddress;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;
@Data
public class UserAddressUpdateRequest {
    private Long userId;
    @JsonProperty("address_name")
    private String addressName;
    private String code;
    @JsonProperty("region_1depth_name")
    private String region1DepthName;
    @JsonProperty("region_2depth_name")
    private String region2DepthName;
    @JsonProperty("region_3depth_name")
    private String region3DepthName;
    @JsonProperty("region_4depth_name")
    private String region4DepthName;
    private Double x;
    private Double y;
    public UserAddress toEntityUserAddress() {
        return UserAddress.builder()
                .userId(this.userId)
                .addressName(this.addressName)
                .code(this.code)
                .region1DepthName(this.region1DepthName)
                .region2DepthName(this.region2DepthName)
                .region3DepthName(this.region3DepthName)
                .region4DepthName(this.region4DepthName)
                .x(this.x)
                .y(this.y).build();
    }
    public void UserAddressUpdate(UserAddress entity){
        entity.setAddressName(this.addressName);
        entity.setCode(this.code);
        entity.setRegion1DepthName(this.region1DepthName);
        entity.setRegion2DepthName(this.region2DepthName);
        entity.setRegion3DepthName(this.region3DepthName);
        entity.setRegion4DepthName(this.region4DepthName);
        entity.setX(this.x);
        entity.setY(this.y);
    }
}
