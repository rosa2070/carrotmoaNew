package carrotmoa.carrotmoa.model.response;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
//@Setter
@Builder
public class AccommodationResultResponse {
    private Long id;
    private String title;
    private String roadAddress;
    private BigDecimal price;
    private String imageUrl;
    private List<Integer> roomTypeCount;

    public static AccommodationResultResponse fromData(Object[] data) {
        List<Integer> roomTypeCounts = Arrays.asList(
                ((Long) data[5]).intValue(),
                ((Long) data[6]).intValue(),
                ((Long) data[7]).intValue(),
                ((Long) data[8]).intValue()
        );

        return AccommodationResultResponse.builder()
            .id((Long) data[0])
            .title((String) data[1])
            .roadAddress((String) data[2])
            .price((BigDecimal) data[3])
            .imageUrl((String) data[4])
            .roomTypeCount(roomTypeCounts)
            .build();
    }
}