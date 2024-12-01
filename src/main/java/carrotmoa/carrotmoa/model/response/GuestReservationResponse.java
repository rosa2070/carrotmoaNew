package carrotmoa.carrotmoa.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestReservationResponse {
    private Long accommodationId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer status;
    private BigDecimal totalPrice;
    private String title;
    private String lotAddress;
    private String detailAddress;
    private Integer floor;
    private String imageUrl;
    private String impUid;
    private Long reservationId; // 추가
}
