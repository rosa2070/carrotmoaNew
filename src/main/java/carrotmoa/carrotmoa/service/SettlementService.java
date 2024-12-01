package carrotmoa.carrotmoa.service;

import carrotmoa.carrotmoa.model.response.PaymentDetailResponse;
import carrotmoa.carrotmoa.repository.PaymentDetailCustomRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SettlementService {

    private final PaymentDetailCustomRepository paymentDetailCustomRepository;

    public SettlementService(PaymentDetailCustomRepository paymentDetailCustomRepository) {
        this.paymentDetailCustomRepository = paymentDetailCustomRepository;
    }

    public List<PaymentDetailResponse> getSettlementList(Long hostId, Long accommodationId, LocalDate startDate, LocalDate endDate) {
        return paymentDetailCustomRepository.getSettlementList(hostId, accommodationId, startDate, endDate);
    }


}
