package carrotmoa.carrotmoa.model.request;

import carrotmoa.carrotmoa.entity.Payment;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    //    private Long partnerId;
    private Long userId;
    private Long reservationId;
    private LocalDate paymentDate;

    @JsonProperty("imp_uid")
    private String impUid; // 결제 성공시 응답 객체에서 제공(rsp 객체에 포함)

    @JsonProperty("pay_method")
    private String payMethod;

    @JsonProperty("merchant_uid")
    private String merchantUid;

    @JsonProperty("paid_amount") //
    private int paidAmount;

    @JsonProperty("pg_provider")
    private String pgProvider;

    @JsonProperty("pg_type")
    private String pgType;

    @JsonProperty("pg_tid")
    private String pgTid;

    private String status;

    @JsonProperty("card_name")
    private String cardName;

    @JsonProperty("card_number")
    private String cardNumber;

    public Payment toPaymentEntity() {
        return Payment.builder()
//                .partnerId(partnerId)
            .userId(userId)
            .reservationId(reservationId)
            .impUid(impUid)
            .paymentMethod(payMethod)
            .merchantUid(merchantUid)
            .paymentAmount(BigDecimal.valueOf(paidAmount))
            .pgProvider(pgProvider)
            .pgType(pgType)
            .pgTid(pgTid)
            .status(status)
            .cardName(cardName)
            .cardNumber(cardNumber)
//                .paymentDate(LocalDateTime.now()) // 필요에 따라 현재 시간을 설정
            .build();
    }


}
