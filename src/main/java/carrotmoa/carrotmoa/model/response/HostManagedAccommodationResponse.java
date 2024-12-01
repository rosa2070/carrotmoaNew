package carrotmoa.carrotmoa.model.response;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HostManagedAccommodationResponse {
    private Long id;
    private String title;
    private String lotAddress;
    private String detailAddress;
    private BigDecimal price;
    private String imageUrl;


}
