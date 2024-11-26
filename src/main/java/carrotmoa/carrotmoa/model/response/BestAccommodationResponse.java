package carrotmoa.carrotmoa.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class BestAccommodationResponse {
    Long id;
    String title;
    String lotAddress;
    BigDecimal price;
    String imageUrl;
    Long reservationCount;
}
