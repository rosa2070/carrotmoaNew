package carrotmoa.carrotmoa.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Builder
@Slf4j
public class BookingListResponse {
    // reservation table
    private Long id;
    private Long accommodationId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer status;
    private BigDecimal totalPrice;
    // post table
    private String title;
    // accommodation table
    private String lotAddress;
    private String detailAddress;
    private Integer floor;
    private String imageUrl;
    // userProfile
    private String nickname;

    public static BookingListResponse fromData(Object[] data) {
        return BookingListResponse.builder()
            .id((Long) data[0])
            .accommodationId((Long) data[1])
            .checkInDate((LocalDate) data[2])
            .checkOutDate((LocalDate) data[3])
            .status((Integer) data[4])
            .totalPrice((BigDecimal) data[5])
            .title((String) data[6])
            .lotAddress((String) data[7])
            .detailAddress((String) data[8])
            .floor((Integer) data[9])
            .nickname((String) data[10])
            .imageUrl((String) data[11])
            .build();
    }
}
