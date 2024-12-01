package carrotmoa.carrotmoa.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAndReservationRequest {
    private PaymentRequest paymentRequest;
    private ReservationRequest reservationRequest;
}
