package carrotmoa.carrotmoa.model.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetailResponse {
    private LocalDate settlementDate; // 체크인 날짜 + 1
    private String title;
    private String nickname; // 유저 이름
    private LocalDate checkInDate;
    private BigDecimal paymentAmount;
}
