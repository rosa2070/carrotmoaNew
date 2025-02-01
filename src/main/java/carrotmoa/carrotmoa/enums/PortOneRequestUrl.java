package carrotmoa.carrotmoa.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PortOneRequestUrl {

    ACCESS_TOKEN_URL("/users/getToken"), // 토큰 발행 경로
    CANCEL_PAYMENT_URL("/payments/cancel"), // 포트원에서 결제 취소해주는 경로
    CREATE_PAYMENT_URL("/payments/"); // 포트원에서 전체 결제 내역을 볼 수 있는 경로

    private final String url;

}
