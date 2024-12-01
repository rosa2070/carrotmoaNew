package carrotmoa.carrotmoa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user_address")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "address_name")
    private String addressName;

    @Column(name = "code")
    private String code;

    @Column(name = "region_1depth_name")
    private String region1DepthName;

    @Column(name = "region_2depth_name")
    private String region2DepthName;

    @Column(name = "region_3depth_name")
    private String region3DepthName;

    @Column(name = "region_4depth_name")
    private String region4DepthName;

    @Column(name = "x")
    private Double x;

    @Column(name = "y")
    private Double y;
}
